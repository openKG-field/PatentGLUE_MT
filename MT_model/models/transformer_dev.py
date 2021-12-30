import torch
import torch.nn as nn
import torch.nn.functional as F

"""
The development version of transformer
"""


class Transformer(nn.Module):
    def __init__(self, n_src_words, n_tgt_words, src_pdx=-1, tgt_pdx=-1, d_model=512, d_ff=2048, n_head=8, 
                 n_layers=6, n_encoder_layers=None, n_decoder_layers=None, p_drop=0.1, max_seq_len=512) -> None:

        super().__init__()
        self.d_model = d_model
        self.src_pdx, self.tgt_pdx = src_pdx, tgt_pdx  # pdx: padding index
        
        self.encoder = Encoder(n_src_words, src_pdx=src_pdx, n_head=n_head, d_model=d_model, d_ff=d_ff, 
                               n_layers=n_layers if n_encoder_layers is None else n_encoder_layers, 
                               p_drop=p_drop, max_seq_len=max_seq_len)

        self.decoder = Decoder(n_tgt_words, tgt_pdx=tgt_pdx, n_head=n_head, d_model=d_model, d_ff=d_ff,
                               n_layers=n_layers if n_decoder_layers is None else n_decoder_layers, 
                               p_drop=p_drop, max_seq_len=max_seq_len)
        self.out_vocab_proj = nn.Linear(d_model, n_tgt_words)
        
        self._model_init()

    def forward(self, src_tokens, prev_tgt_tokens):
        '''
        params:
          - src_tokens: (batch_size, src_len)
          - prev_tgt_tokens: (batch_size, tgt_len)
        
        returns:
          - model_out: (batch_size, tgt_len, n_tgt_words)
        '''

        src_mask = src_tokens.eq(self.src_pdx)
        tgt_mask = prev_tgt_tokens.eq(self.tgt_pdx)

        encoder_out = self.encoder(src_tokens, src_mask)
        decoder_out = self.decoder(
            prev_tgt_tokens, encoder_out, src_mask, tgt_mask)
        model_out = self.out_vocab_proj(decoder_out)
        return model_out

    def _model_init(self):
        for p in self.parameters():
            if p.dim() > 1:
                nn.init.xavier_uniform_(p)


class Encoder(nn.Module):
    def __init__(self, n_src_words, src_pdx, n_head, d_model, d_ff,
                 n_layers, p_drop, max_seq_len) -> None:
        super().__init__()
        self.d_model = d_model
        self.dropout = nn.Dropout(p=p_drop)
        self.input_embedding = nn.Embedding(
            num_embeddings=n_src_words, embedding_dim=d_model, padding_idx=src_pdx)
        self.positional_encode = PositionalEncode(d_model, max_seq_len)
        self.layers = nn.ModuleList(
            [EncoderLayer(d_model, d_ff, n_head, p_drop) for _ in range(n_layers)])
        self.layer_norm = nn.LayerNorm(d_model) # for memory

    def forward(self, src_tokens, src_mask, **kwargs):
        # - src_embed: (batch_size, src_len, d_model)
        src_embed = self.input_embedding(src_tokens) * (self.d_model ** 0.5)
        x = self.dropout(self.positional_encode(src_embed))
        for layer in self.layers:
            x = layer(x, src_mask)
        encoder_out = self.layer_norm(x)
        return encoder_out


class EncoderLayer(nn.Module):
    def __init__(self, d_model, d_ff, n_head, p_drop) -> None:
        super().__init__()
        self.dropout = nn.Dropout(p=p_drop)
        self.prenorm1 = nn.LayerNorm(d_model)
        self.self_attn = MultiHeadAttention(d_model, n_head)
        self.prenorm2 = nn.LayerNorm(d_model)
        self.pos_wise_ffn = FeedForwardNetwork(d_model, d_ff)

    def forward(self, x, src_mask):
        x_norm = self.prenorm1(x)
        x = x + self.dropout(self.self_attn(
            q=x_norm, k=x_norm, v=x_norm, mask=src_mask.unsqueeze(1).unsqueeze(1)))
        x_norm = self.prenorm2(x)
        x = x + self.dropout(self.pos_wise_ffn(x_norm))
        return x


class Decoder(nn.Module):
    def __init__(self, n_tgt_words, tgt_pdx, n_head, d_model, d_ff,
                 n_layers, p_drop, max_seq_len) -> None:
        super().__init__()
        self.d_model = d_model
        self.dropout = nn.Dropout(p=p_drop)
        self.input_embedding = nn.Embedding(
            num_embeddings=n_tgt_words, embedding_dim=d_model, padding_idx=tgt_pdx)
        self.positional_encode = PositionalEncode(d_model, max_seq_len)
        self.layers = nn.ModuleList(
            [DecoderLayer(d_model, d_ff, n_head, p_drop) for _ in range(n_layers)])

    def forward(self, prev_tgt_tokens, encoder_out, src_mask, tgt_mask, **kwargs):
        # - tgt_embed: (batch_size, src_len, d_model)
        tgt_embed = self.input_embedding(prev_tgt_tokens) * (self.d_model ** 0.5)
        x = self.dropout(self.positional_encode(tgt_embed))
        for layer in self.layers:
            x = layer(x, encoder_out, src_mask, tgt_mask)
        # - decoder_out: (batch_size, tgt_len, n_tgt_words)
        decoder_out = x
        return decoder_out


class DecoderLayer(nn.Module):
    def __init__(self, d_model, d_ff, n_head, p_drop) -> None:
        super().__init__()
        self.dropout = nn.Dropout(p=p_drop)
        self.prenorm1 = nn.LayerNorm(d_model)
        self.masked_self_attn = MultiHeadAttention(d_model, n_head)
        self.prenorm2 = nn.LayerNorm(d_model)
        self.context_attn = MultiHeadAttention(d_model, n_head)
        self.prenorm3 = nn.LayerNorm(d_model)
        self.pos_wise_ffn = FeedForwardNetwork(d_model, d_ff)

    def forward(self, x, memory, src_mask, tgt_mask):
        x_norm = self.prenorm1(x)
        x = x + self.dropout(self.masked_self_attn(
            q=x_norm, k=x_norm, v=x_norm, mask=self._add_subsequent_mask(tgt_mask)))
        x_norm = self.prenorm2(x)
        x = x + self.dropout(self.context_attn(
            q=x_norm, k=memory, v=memory, mask=src_mask.unsqueeze(1).unsqueeze(1)))
        x_norm = self.prenorm3(x)
        x = x + self.dropout(self.pos_wise_ffn(x_norm))
        return x

    def _add_subsequent_mask(self, padding_mask):
        if padding_mask == None:
            return None
        # - padding_mask: (batch_size, seq_len)
        seq_len = padding_mask.size(1)
        subsequent_mask = torch.ones((seq_len, seq_len), 
            device=padding_mask.device).triu(diagonal=1).bool()
        # - return: (batch_size, 1, seq_len, seq_len)
        return padding_mask.unsqueeze(1).unsqueeze(1) | subsequent_mask


class MultiHeadAttention(nn.Module):
    # - src_embed_dim = d_model
    def __init__(self, d_model, n_head) -> None:
        super().__init__()
        self.n_head, self.one_head_dim = n_head, d_model // n_head
        self.w_q = nn.Linear(d_model, self.one_head_dim * self.n_head, bias=True)
        self.w_k = nn.Linear(d_model, self.one_head_dim * self.n_head, bias=True)
        self.w_v = nn.Linear(d_model, self.one_head_dim * self.n_head, bias=True)
        self.w_out = nn.Linear(self.one_head_dim * self.n_head, d_model, bias=True)

    def forward(self, q, k, v, mask=None):
        # - x: (batch_size, seq_len, d_model)
        batch_size, q_len, kv_len = q.size(0), q.size(1), k.size(1)
        Q = self.w_q(q).view(batch_size, q_len, self.n_head, 
                             self.one_head_dim).transpose(1, 2)
        K = self.w_k(k).view(batch_size, kv_len, self.n_head,
                             self.one_head_dim).transpose(1, 2)
        V = self.w_v(v).view(batch_size, kv_len, self.n_head,
                             self.one_head_dim).transpose(1, 2)
        # - Q, K, V: (batch_size, n_head, seq_len, one_head_dim)

        Q_KT = torch.matmul(Q, torch.transpose(K, 2, 3))

        if mask != None:
            Q_KT.masked_fill_(mask, -1e9)

        attn = F.softmax(Q_KT / self.one_head_dim ** 0.5, dim=-1)

        O = self.w_out(torch.matmul(attn, V).transpose(1, 2).reshape(
                batch_size, q_len, self.one_head_dim * self.n_head))
        # - O: (batch_size, seq_len, d_model)
        return O


class FeedForwardNetwork(nn.Module):
    def __init__(self, d_model, d_ff) -> None:
        super().__init__()
        self.linear1 = nn.Linear(d_model, d_ff, bias=True)
        self.linear2 = nn.Linear(d_ff, d_model, bias=True)

    def forward(self, x):
        return self.linear2(F.relu(self.linear1(x)))


class PositionalEncode(nn.Module):
    def __init__(self, d_model, max_seq_len=512) -> None:
        super().__init__()
        self.pos_encode = self._get_pos_encode(max_seq_len, d_model)

    def forward(self, x):
        # - x: (batch_size, seq_len, d_model)
        return x + self.pos_encode[:x.size(1), :].unsqueeze(0).to(x.device)

    def _get_pos_encode(self, max_seq_len, d_model):
        # TODO: 尝试使用矩阵乘法，观察哪种方式速度更快
        pos_encode = torch.tensor([[pos / 10000 ** (2 * (i//2) / d_model) for i in range(d_model)]
                                   for pos in range(max_seq_len)], requires_grad=False)
        pos_encode[:, 0::2] = torch.sin(pos_encode[:, 0::2])
        pos_encode[:, 1::2] = torch.cos(pos_encode[:, 1::2])
        # - pos_encode: (seq_len, d_model)
        return pos_encode