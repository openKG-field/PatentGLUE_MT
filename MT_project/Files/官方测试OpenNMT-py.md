## 准备阶段：

```shell
pip install OpenNMT-py
wget https://s3.amazonaws.com/opennmt-trainingdata/toy-ende.tar.gz
tar xf toy-ende.tar.gz
cd toy-ende
```

新建文件 `toy_en_de.yaml`

文件内容：

输入命令:

```
onmt_build_vocab -config toy_en_de.yaml -n_sample 10000
```

得到如下

> Corpus corpus_1's weight should be given. We default it to 1 for you.
> [2021-10-12 21:21:28,212 INFO] Counter vocab from 10000 samples.
> [2021-10-12 21:21:28,212 INFO] Build vocab on 10000 transformed examples/corpus.
> [2021-10-12 21:21:28,243 INFO] corpus_1's transforms: TransformPipe()
> [2021-10-12 21:21:28,952 INFO] Counters src:24995
> [2021-10-12 21:21:28,952 INFO] Counters tgt:35816

## 训练模型

新建文件 `toy_en_de2.yaml`

文件内容

```yaml
# Vocabulary files that were just created
src_vocab: toy-ende/run/example.vocab.src
tgt_vocab: toy-ende/run/example.vocab.tgt

# Train on a single GPU
world_size: 1
gpu_ranks: [0]

# Where to save the checkpoints
save_model: toy-ende/run/model
save_checkpoint_steps: 500
train_steps: 1000
valid_steps: 500
```

实行命令

```yaml
onmt_train -config toy_en_de.yaml
```

训练指令参考网站：[1](https://opennmt.net/OpenNMT-py/options/train.html), [2](https://opennmt.net/OpenNMT-py/FAQ.html)




训练打印结果：

> ```
> linux@13952b326be9:~/lab/NLP/OpenNMT-py$ onmt_train -config ./toy_en_de.yaml
> [2021-10-12 22:10:31,018 INFO] Missing transforms field for corpus_1 data, set to default: [].
> [2021-10-12 22:10:31,018 WARNING] Corpus corpus_1's weight should be given. We default it to 1 for you.
> [2021-10-12 22:10:31,018 INFO] Missing transforms field for valid data, set to default: [].
> [2021-10-12 22:10:31,018 INFO] Parsed 2 corpora from -data.
> [2021-10-12 22:10:31,018 INFO] Get special vocabs from Transforms: {'src': set(), 'tgt': set()}.
> [2021-10-12 22:10:31,018 INFO] Loading vocab from text file...
> [2021-10-12 22:10:31,019 INFO] Loading src vocabulary from toy-ende/run/example.vocab.src
> [2021-10-12 22:10:31,108 INFO] Loaded src vocab has 24995 tokens.
> [2021-10-12 22:10:31,131 INFO] Loading tgt vocabulary from toy-ende/run/example.vocab.tgt
> [2021-10-12 22:10:31,264 INFO] Loaded tgt vocab has 35816 tokens.
> [2021-10-12 22:10:31,289 INFO] Building fields with vocab in counters...
> [2021-10-12 22:10:31,356 INFO]  * tgt vocab size: 35820.
> [2021-10-12 22:10:31,401 INFO]  * src vocab size: 24997.
> [2021-10-12 22:10:31,403 INFO]  * src vocab size = 24997
> [2021-10-12 22:10:31,403 INFO]  * tgt vocab size = 35820
> [2021-10-12 22:10:31,512 INFO] Building model...
> [2021-10-12 22:11:00,640 INFO] NMTModel(
>   (encoder): RNNEncoder(
>     (embeddings): Embeddings(
>       (make_embedding): Sequential(
>         (emb_luts): Elementwise(
>           (0): Embedding(24997, 500, padding_idx=1)
>         )
>       )
>     )
>     (rnn): LSTM(500, 500, num_layers=2, dropout=0.3)
>   )
>   (decoder): InputFeedRNNDecoder(
>     (embeddings): Embeddings(
>       (make_embedding): Sequential(
>         (emb_luts): Elementwise(
>           (0): Embedding(35820, 500, padding_idx=1)
>         )
>       )
>     )
>     (dropout): Dropout(p=0.3, inplace=False)
>     (rnn): StackedLSTM(
>       (dropout): Dropout(p=0.3, inplace=False)
>       (layers): ModuleList(
>         (0): LSTMCell(1000, 500)
>         (1): LSTMCell(500, 500)
>       )
>     )
>     (attn): GlobalAttention(
>       (linear_in): Linear(in_features=500, out_features=500, bias=False)
>       (linear_out): Linear(in_features=1000, out_features=500, bias=False)
>     )
>   )
>   (generator): Sequential(
>     (0): Linear(in_features=500, out_features=35820, bias=True)
>     (1): Cast()
>     (2): LogSoftmax(dim=-1)
>   )
> )
> [2021-10-12 22:11:00,641 INFO] encoder: 16506500
> [2021-10-12 22:11:00,642 INFO] decoder: 41613820
> [2021-10-12 22:11:00,642 INFO] * number of parameters: 58120320
> [2021-10-12 22:11:00,644 INFO] Starting training on GPU: [0]
> [2021-10-12 22:11:00,644 INFO] Start training loop and validate every 500 steps...
> [2021-10-12 22:11:00,645 INFO] corpus_1's transforms: TransformPipe()
> [2021-10-12 22:11:00,645 INFO] Weighted corpora loaded so far:
>                         * corpus_1: 1
> [2021-10-12 22:11:13,850 INFO] Step 50/ 1000; acc:   3.53; ppl: 191969.86; xent: 12.17; lr: 1.00000; 5340/5327 tok/s;     13 sec
> [2021-10-12 22:11:25,420 INFO] Step 100/ 1000; acc:   4.40; ppl: 41524.76; xent: 10.63; lr: 1.00000; 6368/6302 tok/s;     25 sec
> [2021-10-12 22:11:31,543 INFO] Weighted corpora loaded so far:
>                         * corpus_1: 2
> [2021-10-12 22:11:36,425 INFO] Step 150/ 1000; acc:   5.53; ppl: 7634.82; xent: 8.94; lr: 1.00000; 6226/6263 tok/s;     36 sec
> [2021-10-12 22:11:48,054 INFO] Step 200/ 1000; acc:   8.15; ppl: 2956.19; xent: 7.99; lr: 1.00000; 6653/6565 tok/s;     47 sec
> [2021-10-12 22:11:59,087 INFO] Step 250/ 1000; acc:   9.16; ppl: 2146.19; xent: 7.67; lr: 1.00000; 6350/6303 tok/s;     58 sec
> [2021-10-12 22:12:07,693 INFO] Weighted corpora loaded so far:
>                         * corpus_1: 3
> [2021-10-12 22:12:10,733 INFO] Step 300/ 1000; acc:   9.47; ppl: 1874.39; xent: 7.54; lr: 1.00000; 6357/6342 tok/s;     70 sec
> [2021-10-12 22:12:21,725 INFO] Step 350/ 1000; acc:  10.61; ppl: 1487.08; xent: 7.30; lr: 1.00000; 6299/6309 tok/s;     81 sec
> [2021-10-12 22:12:33,197 INFO] Step 400/ 1000; acc:  11.06; ppl: 1365.51; xent: 7.22; lr: 1.00000; 6279/6190 tok/s;     93 sec
> [2021-10-12 22:12:44,318 INFO] Weighted corpora loaded so far:
>                         * corpus_1: 4
> [2021-10-12 22:12:44,868 INFO] Step 450/ 1000; acc:  11.63; ppl: 1151.47; xent: 7.05; lr: 1.00000; 6179/6210 tok/s;    104 sec
> [2021-10-12 22:12:55,921 INFO] Step 500/ 1000; acc:  12.37; ppl: 1044.92; xent: 6.95; lr: 1.00000; 6324/6325 tok/s;    115 sec
> [2021-10-12 22:12:55,923 INFO] valid's transforms: TransformPipe()
> [2021-10-12 22:12:55,923 INFO] valid's transforms: TransformPipe()
> [2021-10-12 22:13:01,662 INFO] Validation perplexity: 1179.66
> [2021-10-12 22:13:01,662 INFO] Validation accuracy: 12.2974
> [2021-10-12 22:13:01,922 INFO] Saving checkpoint toy-ende/run/model_step_500.pt
> [2021-10-12 22:13:14,903 INFO] Step 550/ 1000; acc:  12.64; ppl: 974.72; xent: 6.88; lr: 1.00000; 3995/3912 tok/s;    134 sec
> [2021-10-12 22:13:25,742 INFO] Step 600/ 1000; acc:  13.19; ppl: 880.30; xent: 6.78; lr: 1.00000; 6405/6434 tok/s;    145 sec
> [2021-10-12 22:13:27,779 INFO] Weighted corpora loaded so far:
>                         * corpus_1: 5
> [2021-10-12 22:13:37,740 INFO] Step 650/ 1000; acc:  13.28; ppl: 830.30; xent: 6.72; lr: 1.00000; 6251/6209 tok/s;    157 sec
> [2021-10-12 22:13:50,207 INFO] Step 700/ 1000; acc:  13.93; ppl: 756.33; xent: 6.63; lr: 1.00000; 5596/5547 tok/s;    170 sec
> [2021-10-12 22:14:03,749 INFO] Step 750/ 1000; acc:  14.03; ppl: 718.94; xent: 6.58; lr: 1.00000; 5425/5413 tok/s;    183 sec
> [2021-10-12 22:14:08,454 INFO] Weighted corpora loaded so far:
>                         * corpus_1: 6
> [2021-10-12 22:14:16,941 INFO] Step 800/ 1000; acc:  14.55; ppl: 654.48; xent: 6.48; lr: 1.00000; 5435/5425 tok/s;    196 sec
> [2021-10-12 22:14:29,517 INFO] Step 850/ 1000; acc:  14.83; ppl: 618.77; xent: 6.43; lr: 1.00000; 5618/5540 tok/s;    209 sec
> [2021-10-12 22:14:41,343 INFO] Step 900/ 1000; acc:  14.83; ppl: 599.77; xent: 6.40; lr: 1.00000; 6172/6203 tok/s;    221 sec
> [2021-10-12 22:14:47,607 INFO] Weighted corpora loaded so far:
>                         * corpus_1: 7
> [2021-10-12 22:14:52,283 INFO] Step 950/ 1000; acc:  15.66; ppl: 543.35; xent: 6.30; lr: 1.00000; 6278/6323 tok/s;    232 sec
> [2021-10-12 22:15:03,687 INFO] Step 1000/ 1000; acc:  15.50; ppl: 523.70; xent: 6.26; lr: 1.00000; 6827/6664 tok/s;    243 sec
> [2021-10-12 22:15:09,449 INFO] Validation perplexity: 989.246
> [2021-10-12 22:15:09,449 INFO] Validation accuracy: 12.1662
> [2021-10-12 22:15:09,754 INFO] Saving checkpoint toy-ende/run/model_step_1000.pt
> ```


## 翻译检测

翻译输入

```shell
onmt_translate -model toy-ende/run/model_step_1000.pt -src toy-ende/src-test.txt -output toy-ende/pred_1000.txt -gpu 0 -verbose
```

翻译结果

```
[2021-10-12 22:22:44,381 INFO] PRED AVG SCORE: -1.9891, PRED PPL: 7.3091
```

翻译样例：

>
> [2021-10-12 22:22:43,805 INFO]
> SENT 2637: ['&quot;', 'The', 'greater', 'hope', 'has', 'been', 'well', 'and', 'truly', 'cast', 'out', 'of', 'Christians', 'by', 'religious', 'critics', ',', '&quot;', 'said', 'the', 'speaker', '.']
> PRED 2637: Mit der EU des Europäischen Parlaments wird die Möglichkeit des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -41.0780
>
> [2021-10-12 22:22:43,805 INFO]
> SENT 2638: ['They', 'have', '&quot;', 'provided', 'us', 'with', 'so', 'many', 'question', 'marks', ',', 'that', 'we', 'seem', 'to', 'have', 'lost', 'our', 'exclamation', 'marks', '&quot;', '.']
> PRED 2638: Es ist die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -36.7554
>
> [2021-10-12 22:22:43,805 INFO]
> SENT 2639: ['Johanna', 'Rahner', 'continued:', '&quot;', 'However', ',', 'the', 'crucial', 'challenge', 'to', 'this', 'remains', 'fear', 'of', 'death', '&quot;', '.']
> PRED 2639: Mit der Nähe des Europäischen Parlaments wird die Möglichkeit des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -36.7411
>
> [2021-10-12 22:22:43,806 INFO]
> SENT 2640: ['Associated', 'with', 'this', 'is', 'the', 'matter', 'of', 'that', 'longing', ',', '&quot;', 'that', 'dreams', 'that', 'everything', 'will', 'be', 'good', 'at', 'the', 'end', '&quot;', '.']
> PRED 2640: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -36.9857
>
> [2021-10-12 22:22:43,940 INFO]
> SENT 2641: ['Johanna', 'Rahner', ',', 'born', 'in', '1962', 'in', 'Baden-Baden', ',', 'studied', 'Catholic', 'theology', 'and', 'biology', 'from', '1982', 'to', '1989', 'at', 'the', 'Albert', 'Ludwig', '&apos;s', 'University', 'in', 'Freiburg', '.']
> PRED 2641: Mit der EU des Europäischen Parlaments wird die Kommission des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -46.0816
>
> [2021-10-12 22:22:43,940 INFO]
> SENT 2642: ['In', '1997', 'she', 'graduated', 'as', 'a', 'doctor', 'of', 'theology', ',', 'also', 'in', 'Freiburg', '.']
> PRED 2642: In der EU des Europäischen Parlaments werden die Frage des Europäischen Parlaments .
> PRED SCORE: -28.6583
>
> [2021-10-12 22:22:43,940 INFO]
> SENT 2643: ['A', 'post-doctoral', 'lecturing', 'qualification', 'in', 'fundamental', 'theology', 'and', 'ecumenical', 'theology', 'followed', 'in', '2003', 'at', 'the', 'Westfalian', 'Wilhelm', '&apos;s', 'University', '.']
> PRED 2643: Im Rahmen des Europäischen Parlaments wird die Möglichkeit des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -40.3159
>
> [2021-10-12 22:22:43,940 INFO]
> SENT 2644: ['Since', '2010', ',', 'Johanna', 'Rahner', 'has', 'occupied', 'a', 'Chair', 'for', 'Systematic', 'Theology', 'in', 'the', 'Institute', 'for', 'Catholic', 'Theology', 'at', 'the', 'University', 'of', 'Kassel', '.']
> PRED 2644: Im Rahmen des Europäischen Parlaments hat die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -39.0936
>
> [2021-10-12 22:22:43,940 INFO]
> SENT 2645: ['Germany', '&apos;s', 'largest', 'national', 'church', 'has', 'launched', 'a', 'campaign', 'advertising', 'the', 'minister', '&apos;s', 'position', '.']
> PRED 2645: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments .
> PRED SCORE: -27.7760
>
> [2021-10-12 22:22:43,940 INFO]
> SENT 2646: ['This', 'comes', 'at', 'the', 'same', 'time', 'as', 'a', 'possible', 'shortage', 'of', 'ministers:', 'The', 'Protestant', 'Lutheran', 'Regional', 'Church', 'of', 'Hanover', 'calculates', 'that', ',', 'as', 'things', 'stand', ',', 'the', 'current', 'number', 'of', 'around', '1,800', 'pastors', 'will', 'be', 'halved', 'by', '2030', '.']
> PRED 2646: Dies ist die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -52.2362
>
> [2021-10-12 22:22:43,940 INFO]
> SENT 2647: ['For', 'the', 'young', 'people', 'of', 'today', ',', 'the', 'career', 'is', 'very', 'attractive', 'and', 'offers', 'good', 'future', 'prospects', ',', 'said', 'Pastor', 'Mathis', 'Burfien', '(43)', 'in', 'a', 'conversation', 'with', 'the', 'Protestant', 'Press', 'Service', '.']
> PRED 2647: Wir haben die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -47.2481
>
> [2021-10-12 22:22:43,941 INFO]
> SENT 2648: ['It', 'is', 'attractive', 'to', 'be', 'able', 'to', 'determine', 'your', 'own', 'daily', 'working', 'routine', '.']
> PRED 2648: Es ist die Frage des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -19.5780
>
> [2021-10-12 22:22:43,941 INFO]
> SENT 2649: ['With', 'Burfien', ',', 'the', 'regional', 'church', 'has', 'for', 'first', 'time', 'commissioned', 'a', 'minister', 'to', 'work', 'full', 'time', ',', 'endeavouring', 'to', 'inspire', 'young', 'people', 'to', 'study', 'theology', '.']
> PRED 2649: Mit der EU des Europäischen Parlaments werden die Kommission des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -45.3405
>
> [2021-10-12 22:22:43,941 INFO]
> SENT 2650: ['At', 'present', ',', 'fewer', 'and', 'fewer', 'young', 'people', 'are', 'deciding', 'to', 'study', 'theology', 'after', 'their', 'Abitur', 'examinations', '(equivalent', 'to', 'A', 'levels).']
> PRED 2650: Im Rahmen des Europäischen Parlaments hat die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -33.8940
>
> [2021-10-12 22:22:43,941 INFO]
> SENT 2651: ['Burfien', 'puts', 'this', 'down', 'to', 'the', 'process', 'of', 'secularisation:', '&quot;', 'God', '&apos;s', 'voice', 'is', 'quiet', ',', 'the', 'world', 'is', 'loud', '&quot;', '.']
> PRED 2651: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -37.0867
>
> [2021-10-12 22:22:43,941 INFO]
> SENT 2652: ['The', 'job', 'is', 'characterised', 'by', 'great', 'freedom', 'and', 'variety', '.']
> PRED 2652: Der Hotel des Europäischen Parlaments wird die Frage der EU .
> PRED SCORE: -26.8958
>
> [2021-10-12 22:22:43,941 INFO]
> SENT 2653: ['I', 'am', 'the', 'master', 'of', 'my', 'own', 'schedule', 'and', 'can', 'therefore', 'turn', 'my', 'focus', 'to', 'what', 'is', 'important', 'to', 'me', '.']
> PRED 2653: Ich habe die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -28.9501
>
> [2021-10-12 22:22:43,941 INFO]
> SENT 2654: ['As', 'pastoral', 'workers', ',', 'ministers', 'can', 'be', 'close', 'to', 'people', '.']
> PRED 2654: Herr Präsident !
> PRED SCORE: -8.4378
>
> [2021-10-12 22:22:43,942 INFO]
> SENT 2655: ['They', 'earn', 'as', 'much', 'as', 'teachers', 'and', 'can', 'make', 'a', 'good', 'living', '.']
> PRED 2655: Wir haben die Frage des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -24.8903
>
> [2021-10-12 22:22:43,942 INFO]
> SENT 2656: ['Of', 'course', 'wages', 'are', 'higher', 'within', 'the', 'private', 'sector', ',', 'but', 'on', 'the', 'other', 'hand', ',', 'theologians', 'have', 'a', 'secure', 'employer', '.']
> PRED 2656: Wir haben die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -32.8825
>
> [2021-10-12 22:22:43,942 INFO]
> SENT 2657: ['This', 'would', 'suit', 'the', 'young', 'people', 'of', 'today', ',', 'as', 'it', 'is', 'not', 'just', 'about', 'your', 'career', ',', 'but', 'about', 'having', 'a', 'meaningful', 'job', 'as', 'well', '.']
> PRED 2657: Dies ist die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -36.7434
>
> [2021-10-12 22:22:43,942 INFO]
> SENT 2658: ['According', 'to', 'information', 'from', 'the', 'Regional', 'Church', ',', 'which', 'covers', 'three', 'quarters', 'of', 'Lower', 'Saxony', ',', 'around', '60', 'pastors', 'retire', 'every', 'year', '.']
> PRED 2658: Mit der EU des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -39.3833
>
> [2021-10-12 22:22:43,942 INFO]
> SENT 2659: ['At', 'the', 'same', 'time', ',', 'around', '40', 'theology', 'graduates', 'begin', 'as', 'vicars', '.']
> PRED 2659: Im Rahmen des Europäischen Parlaments hat die Frage des Europäischen Parlaments .
> PRED SCORE: -25.1100
>
> [2021-10-12 22:22:43,943 INFO]
> SENT 2660: ['In', 'future', 'it', 'may', 'be', 'difficult', 'to', 'fill', 'positions', 'in', 'sparsely', 'populated', 'outlying', 'areas', 'such', 'as', 'Harz', ',', 'Emsland', 'or', 'Wendland', '.']
> PRED 2660: Im Rahmen des Europäischen Parlaments werden die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -34.5634
>
> [2021-10-12 22:22:43,943 INFO]
> SENT 2661: ['Among', 'other', 'initiatives', ',', 'Burfien', 'wants', 'to', 'organise', 'study', 'days', 'for', 'young', 'people', ',', 'inviting', 'famous', 'faces', 'who', 'have', 'studied', 'theology', '.']
> PRED 2661: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -37.9336
>
> [2021-10-12 22:22:43,943 INFO]
> SENT 2662: ['Theology', 'courses', 'offer', 'broad', 'training', '.']
> PRED 2662: Das Hotel ist die Möglichkeit .
> PRED SCORE: -15.8434
>
> [2021-10-12 22:22:43,943 INFO]
> SENT 2663: ['You', 'can', 'even', 'become', 'Federal', 'President', 'with', 'a', 'theology', 'degree', '.']
> PRED 2663: Sie haben die Frage des Europäischen Parlaments .
> PRED SCORE: -20.5113
>
> [2021-10-12 22:22:43,943 INFO]
> SENT 2664: ['A', 'kindergarten', 'has', 'found', 'a', 'private', 'buyer']
> PRED 2664: Das Hotel ist die Möglichkeit .
> PRED SCORE: -15.8657
>
> [2021-10-12 22:22:43,943 INFO]
> SENT 2665: ['&quot;', 'The', 'building', 'is', 'in', 'good', 'hands', ',', '&quot;', 'said', 'Winterlingen', '&apos;s', 'Mayor', 'Michael', 'Maier', '.']
> PRED 2665: Mit der EU des Europäischen Parlaments wird die Regierung des Europäischen Parlaments .
> PRED SCORE: -30.1483
>
> [2021-10-12 22:22:43,943 INFO]
> SENT 2666: ['The', 'local', 'authorities', 'have', 'sold', 'the', 'former', 'Kindergarten', 'on', 'Gartenstraße', 'to', 'a', 'private', 'buyer', '.']
> PRED 2666: Der Hotel des Europäischen Parlaments wird die Regierung des Europäischen Parlaments .
> PRED SCORE: -26.7253
>
> [2021-10-12 22:22:43,944 INFO]
> SENT 2667: ['The', 'Town', 'Council', 'consented', 'to', 'the', 'sale', 'in', 'its', 'most', 'recent', 'meeting', ',', 'during', 'the', 'part', 'of', 'the', 'meeting', 'that', 'was', 'not', 'open', 'to', 'the', 'public', '.']
> PRED 2667: Die Kommission des Europäischen Parlaments wird die Regierung des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -45.6100
>
> [2021-10-12 22:22:43,944 INFO]
> SENT 2668: ['The', 'building', 'changed', 'hands', 'for', 'an', '&quot;', 'appropriate', '&quot;', 'price', '.']
> PRED 2668: Der Hotel des Europäischen Parlaments wird die Frage der EU .
> PRED SCORE: -26.7756
>
> [2021-10-12 22:22:43,944 INFO]
> SENT 2669: ['&quot;', 'The', 'local', 'authorities', 'are', 'satisfied', ',', '&quot;', 'emphasised', 'Maier', '.']
> PRED 2669: Im Rahmen des Europäischen Parlaments wird die Möglichkeit .
> PRED SCORE: -22.6039
>
> [2021-10-12 22:22:43,944 INFO]
> SENT 2670: ['EUR', '100,000', 'have', 'been', 'added', 'to', 'the', 'local', 'budget', '.']
> PRED 2670: Im Rahmen des Europäischen Parlaments wird die Frage der EU .
> PRED SCORE: -28.3227
>
> [2021-10-12 22:22:44,119 INFO]
> SENT 2671: ['It', 'is', 'not', 'yet', 'clear', 'how', 'the', 'new', 'owner', 'will', 'use', 'the', 'former', 'Kindergarten', '.']
> PRED 2671: Es ist die Frage des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -19.4883
>
> [2021-10-12 22:22:44,120 INFO]
> SENT 2672: ['The', 'Mayor', 'considers', 'it', 'the', 'right', 'decision', 'that', 'Winterlingen', 'has', 'handed', 'over', 'responsibility', 'for', 'the', 'building', 'to', 'someone', 'else', 'before', 'the', 'winter', ',', 'and', 'thus', 'will', 'no', 'longer', 'have', 'to', 'pay', 'for', 'the', 'upkeep', 'of', 'the', 'building', ',', 'such', 'as', 'heating', 'it', ',', 'tending', 'to', 'the', 'garden', 'and', 'clearing', 'the', 'pavement', '.']
> PRED 2672: Die Kommission des Europäischen Parlaments wird die Kommission des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -80.5659
>
> [2021-10-12 22:22:44,120 INFO]
> SENT 2673: ['&quot;', 'This', 'expense', 'has', 'now', 'been', 'offloaded', ',', '&quot;', 'Maier', 'was', 'glad', 'to', 'say', '.']
> PRED 2673: Dies ist die Frage des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -25.4055
>
> [2021-10-12 22:22:44,120 INFO]
> SENT 2674: ['The', 'throwaway', 'society', 'does', 'not', 'think']
> PRED 2674: Das Hotel hat die Frage der EU .
> PRED SCORE: -19.9396
>
> [2021-10-12 22:22:44,120 INFO]
> SENT 2675: ['The', 'night', 'was', 'long', ',', 'the', 'music', 'loud', 'and', 'the', 'atmosphere', 'good', ',', 'but', 'at', 'some', 'point', 'everyone', 'has', 'to', 'go', 'home', '.']
> PRED 2675: Der Hotel des Europäischen Parlaments wird die Regierung des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -36.4725
>
> [2021-10-12 22:22:44,120 INFO]
> SENT 2676: ['Except:', 'In', 'the', 'stomachs', 'of', 'those', 'in', 'the', 'passenger', 'and', 'back', 'seats', ',', 'hunger', 'strikes', '.']
> PRED 2676: In der EU des Europäischen Parlaments werden die Kommission des Europäischen Parlaments .
> PRED SCORE: -30.7712
>
> [2021-10-12 22:22:44,120 INFO]
> SENT 2677: ['Of', 'course', ',', 'this', 'may', 'be', 'down', 'to', 'the', 'odd', 'cocktail', 'or', 'glass', 'of', 'bubbly', '!']
> PRED 2677: Wir haben die Frage des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -22.7460
>
> [2021-10-12 22:22:44,121 INFO]
> SENT 2678: ['So', 'what', 'could', 'be', 'more', 'natural', 'than', 'to', 'casually', 'pull', 'into', 'the', 'next', 'drive-through', 'to', 'pick', 'yourself', 'up', 'a', 'little', 'treat', '?']
> PRED 2678: Herr Präsident ! Ich möchte die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -31.1725
>
> [2021-10-12 22:22:44,121 INFO]
> SENT 2679: ['I', 'admit', ',', 'I', 'am', 'right', 'with', 'you', 'with', 'it', 'comes', 'to', 'picking', 'up', 'some', 'fast', 'food', 'late', 'at', 'night', ',', 'or', 'early', 'in', 'the', 'morning', 'as', 'the', 'case', 'may', 'be', '.']
> PRED 2679: Ich habe die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -37.7314
>
> [2021-10-12 22:22:44,121 INFO]
> SENT 2680: ['A', 'few', 'chips', ',', 'a', 'coke', ',', 'a', 'burger', '-', 'and', 'then', 'straight', 'home', 'and', 'into', 'bed', '!']
> PRED 2680: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -33.5870
>
> [2021-10-12 22:22:44,121 INFO]
> SENT 2681: ['However', ',', 'on', 'the', 'way', 'home', 'the', 'clear', 'differences', 'between', 'me', 'and', 'other', 'hungry', 'clubbers', 'become', 'evident', '.']
> PRED 2681: Wir haben die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -29.3257
>
> [2021-10-12 22:22:44,121 INFO]
> SENT 2682: ['What', 'is', 'wrong', 'with', 'people', 'who', 'continually', 'throw', 'their', 'fast', 'food', 'bags', 'out', 'of', 'the', 'car', 'window', '?', '!']
> PRED 2682: Herr Präsident ! Ich möchte die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -27.8068
>
> [2021-10-12 22:22:44,121 INFO]
> SENT 2683: ['At', 'the', 'weekend', 'in', 'particular', ',', 'discarded', 'paper', 'bags', 'can', 'be', 'found', 'at', 'the', 'side', 'of', 'the', 'road', 'and', 'in', 'car', 'parks', '.']
> PRED 2683: Im Rahmen des Europäischen Parlaments hat die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -33.9399
>
> [2021-10-12 22:22:44,121 INFO]
> SENT 2684: ['&quot;', 'Why', 'is', 'this', '?', '&quot;', 'I', 'ask', 'myself', '.']
> PRED 2684: Herr Kommissar !
> PRED SCORE: -5.1827
>
> [2021-10-12 22:22:44,122 INFO]
> SENT 2685: ['Are', 'the', 'people', 'too', 'stupid', 'to', 'take', 'their', 'bags', 'home', 'or', 'throw', 'them', 'into', 'the', 'nearest', 'rubbish', 'bin', '?']
> PRED 2685: Herr Präsident ! Ich möchte die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -31.2349
>
> [2021-10-12 22:22:44,122 INFO]
> SENT 2686: ['After', 'all', ',', 'waste', 'paper', 'does', 'more', 'than', 'spoil', 'the', 'landscape', '.']
> PRED 2686: Im Rahmen des Europäischen Parlaments wird die Frage der EU .
> PRED SCORE: -27.0621
>
> [2021-10-12 22:22:44,122 INFO]
> SENT 2687: ['Bags', 'have', 'also', 'been', 'seen', 'covering', 'transport', 'facilities', 'such', 'as', 'crash', 'barriers', ',', 'thus', 'causing', 'accidents', '.']
> PRED 2687: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -32.2113
>
> [2021-10-12 22:22:44,122 INFO]
> SENT 2688: ['However', ',', 'the', 'so-called', '&quot;', 'throwaway', 'society', '&quot;', 'does', 'not', 'think', 'about', 'such', 'things', '.', 'They', 'probably', 'do', 'not', 'even', 'know', 'what', 'the', 'term', 'even', 'means', '.']
> PRED 2688: Es ist die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -36.5937
>
> [2021-10-12 22:22:44,122 INFO]
> SENT 2689: ['If', 'you', 'are', 'often', 'out', 'and', 'about', 'in', 'the', 'evening', ',', 'more', 'often', 'than', 'not', 'with', 'several', 'people', 'in', 'the', 'car', ',', 'you', 'will', 'know', 'what', 'a', 'back', 'seat', 'can', 'look', 'like', 'after', 'having', 'partied', 'through', 'the', 'night:', 'clothes', ',', 'bottles', 'and', 'other', 'rubbish', 'begin', 'to', 'pile', 'up', '.']
> PRED 2689: Wenn Sie die Frage des Europäischen Parlaments für die EU des Europäischen Parlaments mit der Europäischen Parlaments des Europäischen Parlaments für die EU des Europäischen Parlaments mit der EU des Europäischen Parlaments mit der EU des Europäischen Parlaments .
> PRED SCORE: -85.0906
>
> [2021-10-12 22:22:44,122 INFO]
> SENT 2690: ['A', 'few', 'bags', 'won', '&apos;t', 'make', 'much', 'of', 'a', 'difference', 'there', '-', 'in', 'contrast', 'to', 'on', 'the', 'street', 'where', 'no-one', 'wants', 'to', 'trample', 'through', 'other', 'peoples', '&apos;', 'leftover', 'food', '.']
> PRED 2690: Mit der EU des Europäischen Parlaments werden die Kommission des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -52.9445
>
> [2021-10-12 22:22:44,123 INFO]
> SENT 2691: ['I', 'can', 'see', 'the', 'day', 'coming', 'when', 'towns', 'and', 'municipalities', 'will', 'get', 'fed', 'up', 'clearing', 'up', '-', 'and', 'the', 'fast', 'food', 'chains', 'will', 'have', 'to', 'operate', 'a', 'deposit', 'scheme', 'for', 'their', 'bags', '.']
> PRED 2691: Ich habe die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -43.8350
>
> [2021-10-12 22:22:44,123 INFO]
> SENT 2692: ['FAA:', 'Air', 'passengers', 'can', 'now', 'use', 'gadgets', 'on', 'planes', '(but', 'not', 'make', 'cell', 'phone', 'calls)']
> PRED 2692: Mit der EU des Europäischen Parlaments wird die Frage des Europäischen Parlaments .
> PRED SCORE: -30.5446
>
> [2021-10-12 22:22:44,123 INFO]
> SENT 2693: ['Airline', 'passengers', 'will', 'be', 'able', 'to', 'use', 'their', 'electronic', 'devices', 'gate-to-gate', 'to', 'read', ',', 'work', ',', 'play', 'games', ',', 'watch', 'movies', 'and', 'listen', 'to', 'music', '-', 'but', 'not', 'talk', 'on', 'their', 'cellphones', '-', 'under', 'much-anticipated', 'new', 'guidelines', 'issued', 'Thursday', 'by', 'the', 'Federal', 'Aviation', 'Administration', '.']
> PRED 2693: Mit der EU des Europäischen Parlaments wird die Kommission des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments in der Europäischen Parlaments .
> PRED SCORE: -81.1310
>
> [2021-10-12 22:22:44,123 INFO]
> SENT 2694: ['But', 'passengers', 'shouldn', '&apos;t', 'expect', 'changes', 'to', 'happen', 'immediately', '.']
> PRED 2694: Es ist die Frage des Europäischen Parlaments .
> PRED SCORE: -17.3237
>
> [2021-10-12 22:22:44,123 INFO]
> SENT 2695: ['How', 'fast', 'the', 'change', 'is', 'implemented', 'will', 'vary', 'by', 'the', 'airline', ',', 'FAA', 'Administrator', 'Michael', 'Huerta', 'said', 'at', 'a', 'news', 'conference', '.']
> PRED 2695: Herr Präsident ! Ich möchte die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -31.4861
>
> [2021-10-12 22:22:44,123 INFO]
> SENT 2696: ['Airlines', 'will', 'have', 'to', 'show', 'the', 'FAA', 'how', 'their', 'airplanes', 'meet', 'the', 'new', 'guidelines', 'and', 'that', 'they', '&apos;ve', 'updating', 'their', 'flight', 'crew', 'training', 'manuals', 'and', 'rules', 'for', 'stowing', 'devices', 'to', 'reflect', 'the', 'new', 'guidelines', '.']
> PRED 2696: Im Rahmen des Europäischen Parlaments ist die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -53.7287
>
> [2021-10-12 22:22:44,123 INFO]
> SENT 2697: ['The', 'FAA', 'said', 'it', 'has', 'already', 'received', 'plans', 'from', 'some', 'airlines', 'to', 'expand', 'the', 'use', 'of', 'portable', 'electronic', 'devices', 'on', 'planes', '.']
> PRED 2697: Der Hotel des Europäischen Parlaments wird die Regierung des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -36.2927
>
> [2021-10-12 22:22:44,124 INFO]
> SENT 2698: ['Delta', 'and', 'JetBlue', 'were', 'among', 'the', 'airliners', 'who', 'have', 'already', 'submitted', 'plans', '.']
> PRED 2698: Mit der Nähe des Europäischen Parlaments wird die Regierung der EU .
> PRED SCORE: -30.4134
>
> [2021-10-12 22:22:44,124 INFO]
> SENT 2699: ['&quot;', 'Depending', 'on', 'the', 'condition', 'of', 'the', 'plan', ',', 'we', 'could', 'approve', 'expanded', 'use', 'of', 'electronic', 'devices', 'very', 'soon', ',', '&quot;', 'the', 'FAA', 'said', 'in', 'a', 'statement', '.']
> PRED 2699: Sie haben die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments in der Welt des Europäischen Parlaments .
> PRED SCORE: -49.7258
>
> [2021-10-12 22:22:44,124 INFO]
> SENT 2700: ['Currently', ',', 'passengers', 'are', 'required', 'to', 'turn', 'off', 'their', 'smartphones', ',', 'tablets', 'and', 'other', 'devices', 'once', 'a', 'plane', '&apos;s', 'door', 'closes', '.']
> PRED 2700: Mit der EU des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -40.4023
>
> [2021-10-12 22:22:44,298 INFO]
> SENT 2701: ['They', '&apos;re', 'not', 'supposed', 'to', 'restart', 'them', 'until', 'the', 'planes', 'reach', '10,000', 'feet', 'and', 'the', 'captain', 'gives', 'the', 'go-ahead', '.']
> PRED 2701: Es ist die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -33.0527
>
> [2021-10-12 22:22:44,299 INFO]
> SENT 2702: ['Passengers', 'are', 'supposed', 'to', 'turn', 'their', 'devices', 'off', 'again', 'as', 'the', 'plane', 'descends', 'to', 'land', 'and', 'not', 'restart', 'them', 'until', 'the', 'plane', 'is', 'on', 'the', 'ground', '.']
> PRED 2702: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -47.5422
>
> [2021-10-12 22:22:44,299 INFO]
> SENT 2703: ['Under', 'the', 'new', 'guidelines', ',', 'airlines', 'whose', 'planes', 'are', 'properly', 'protected', 'from', 'electronic', 'interference', 'may', 'allow', 'passengers', 'to', 'use', 'the', 'devices', 'during', 'takeoffs', ',', 'landings', 'and', 'taxiing', ',', 'the', 'FAA', 'said', '.']
> PRED 2703: Mit der EU des Europäischen Parlaments wird die Kommission des Europäischen Parlaments des Europäischen Parlaments in der Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -55.5258
>
> [2021-10-12 22:22:44,299 INFO]
> SENT 2704: ['Most', 'new', 'airliners', 'and', 'other', 'planes', 'that', 'have', 'been', 'modified', 'so', 'that', 'passengers', 'can', 'use', 'Wifi', 'at', 'higher', 'altitudes', 'are', 'expected', 'to', 'meet', 'the', 'criteria', '.']
> PRED 2704: Mit der EU des Europäischen Parlaments werden sich in der Nähe des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -44.2218
>
> [2021-10-12 22:22:44,299 INFO]
> SENT 2705: ['Laura', 'Glading', ',', 'president', 'of', 'the', 'Association', 'of', 'Professional', 'Flight', 'Attendants', ',', 'welcomed', 'the', 'changes', '.']
> PRED 2705: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments .
> PRED SCORE: -27.8467
>
> [2021-10-12 22:22:44,299 INFO]
> SENT 2706: ['&quot;', 'Once', 'the', 'new', 'policy', 'is', 'safely', 'implemented', '-', 'and', 'we', '&apos;re', 'going', 'to', 'work', 'closely', 'with', 'the', 'carrier', 'to', 'do', 'that', '-', 'it', 'will', 'be', 'a', 'win-win', ',', '&quot;', 'Glading', 'said', 'in', 'a', 'statement', '.']
> PRED 2706: Mit der EU des Europäischen Parlaments kann die Kommission des Europäischen Parlaments in der Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -61.9103
>
> [2021-10-12 22:22:44,300 INFO]
> SENT 2707: ['We', '&apos;re', 'frankly', 'tired', 'of', 'feeling', 'like', '&apos;', 'hall', 'monitors', '&apos;', 'when', 'it', 'comes', 'to', 'this', 'issue', '.']
> PRED 2707: Wir haben die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments zu unterstützen .
> PRED SCORE: -28.1375
>
> [2021-10-12 22:22:44,300 INFO]
> SENT 2708: ['But', 'connecting', 'to', 'the', 'Internet', 'to', 'surf', ',', 'exchange', 'emails', ',', 'text', 'or', 'download', 'data', 'will', 'still', 'be', 'prohibited', 'below', '10,000', 'feet', ',', 'the', 'agency', 'said', '.']
> PRED 2708: Es ist die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -37.9520
>
> [2021-10-12 22:22:44,300 INFO]
> SENT 2709: ['Passengers', 'will', 'be', 'told', 'to', 'switch', 'their', 'smartphones', ',', 'tablets', 'and', 'other', 'devices', 'to', 'airplane', 'mode', '.']
> PRED 2709: Wir haben die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -28.7694
>
> [2021-10-12 22:22:44,300 INFO]
> SENT 2710: ['So', ',', 'still', 'no', 'Words', 'With', 'Friends', ',', 'the', 'online', 'Scrabble-type', 'game', 'that', 'actor', 'Alec', 'Baldwin', 'was', 'playing', 'on', 'his', 'smartphone', 'in', '2011', 'when', 'he', 'was', 'famously', 'booted', 'off', 'an', 'American', 'Airlines', 'jet', 'for', 'refusing', 'to', 'turn', 'off', 'the', 'device', 'while', 'the', 'plane', 'was', 'parked', 'at', 'the', 'gate', '.']
> PRED 2710: Herr Präsident ! Ich möchte die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -64.4326
>
> [2021-10-12 22:22:44,300 INFO]
> SENT 2711: ['And', 'heavier', 'devices', 'such', 'as', 'laptops', 'will', 'continue', 'to', 'have', 'to', 'be', 'stowed', 'because', 'of', 'concern', 'they', 'might', 'injure', 'someone', 'if', 'they', 'go', 'flying', 'around', 'the', 'cabin', '.']
> PRED 2711: Wenn wir die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -46.3448
>
> [2021-10-12 22:22:44,300 INFO]
> SENT 2712: ['In-flight', 'cellphone', 'calls', 'also', 'will', 'continue', 'to', 'be', 'prohibited', '.']
> PRED 2712: Im Rahmen des Europäischen Parlaments wird die Möglichkeit .
> PRED SCORE: -22.9759
>
> [2021-10-12 22:22:44,300 INFO]
> SENT 2713: ['Regulatory', 'authority', 'over', 'phone', 'calls', 'belongs', 'to', 'the', 'Federal', 'Communications', 'Commission', ',', 'not', 'the', 'FAA', '.']
> PRED 2713: Mit der EU des Europäischen Parlaments wird die Möglichkeit des Europäischen Parlaments .
> PRED SCORE: -30.3152
>
> [2021-10-12 22:22:44,301 INFO]
> SENT 2714: ['FAA', 'may', 'lift', 'ban', 'on', 'some', 'electronic', 'devices', 'during', 'takeoff', 'and', 'landing']
> PRED 2714: Mit der EU des Europäischen Parlaments wird die Möglichkeit .
> PRED SCORE: -24.9651
>
> [2021-10-12 22:22:44,301 INFO]
> SENT 2715: ['Last', 'month', ',', 'National', 'Transportation', 'Safety', 'Board', 'Mark', 'Rosenker', ',', 'a', 'CBS', 'News', 'national', 'transportation', 'safety', 'expert', ',', 'said', 'that', 'cell', 'phones', 'are', 'still', 'considered', 'a', 'risk', '.']
> PRED 2715: Herr Präsident ! Wir haben die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -43.1267
>
> [2021-10-12 22:22:44,301 INFO]
> SENT 2716: ['&quot;', 'Cell', 'phones', ',', 'that', 'really', 'is', 'an', 'issue', ',', 'not', 'just', 'because', 'potentially', 'it', 'could', 'create', 'interference', 'with', 'navigational', 'devices', ',', 'but', 'we', 'do', 'know', ',', 'according', 'to', 'the', 'FCC', ',', 'that', 'it', 'could', 'interfere', 'with', 'cell', 'phone', 'towers', 'when', 'they', '&apos;re', 'in', 'the', 'air', ',', '&quot;', 'Rosenker', 'said', '.']
> PRED 2716: Sie haben die Frage des Europäischen Parlaments des Europäischen Parlaments in der Europäischen Parlaments des Europäischen Parlaments in der Europäischen Parlaments des Europäischen Parlaments mit der Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments in der Welt des Europäischen Parlaments .
> PRED SCORE: -90.5016
>
> [2021-10-12 22:22:44,301 INFO]
> SENT 2717: ['An', 'industry', 'advisory', 'committee', 'created', 'by', 'the', 'FAA', 'to', 'examine', 'the', 'issue', 'recommended', 'last', 'month', 'that', 'the', 'government', 'permit', 'greater', 'use', 'of', 'personal', 'electronic', 'devices', '.']
> PRED 2717: Mit der EU des Europäischen Parlaments wird die Nähe des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -47.6218
>
> [2021-10-12 22:22:44,301 INFO]
> SENT 2718: ['Pressure', 'has', 'been', 'building', 'on', 'the', 'FAA', 'in', 'recent', 'years', 'to', 'ease', 'restrictions', 'on', 'their', 'use', '.']
> PRED 2718: Im Rahmen des Europäischen Parlaments hat die Frage des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -31.3989
>
> [2021-10-12 22:22:44,301 INFO]
> SENT 2719: ['Critics', 'such', 'as', 'Sen.', 'Claire', 'McCaskill', ',', 'D-Mo', '.', ',', 'contend', 'there', 'is', 'no', 'valid', 'safety', 'reason', 'for', 'the', 'prohibitions', '.']
> PRED 2719: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -37.4833
>
> [2021-10-12 22:22:44,302 INFO]
> SENT 2720: ['The', 'restrictions', 'have', 'also', 'become', 'increasingly', 'difficult', 'to', 'enforce', 'as', 'use', 'of', 'the', 'devices', 'has', 'become', 'ubiquitous', '.']
> PRED 2720: Die Kommission des Europäischen Parlaments wird die Kommission des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -31.5779
>
> [2021-10-12 22:22:44,302 INFO]
> SENT 2721: ['Some', 'studies', 'indicate', 'as', 'many', 'as', 'a', 'third', 'of', 'passengers', 'forget', 'or', 'ignore', 'directions', 'to', 'turn', 'off', 'their', 'devices', '.']
> PRED 2721: Wir haben die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -33.3279
>
> [2021-10-12 22:22:44,302 INFO]
> SENT 2722: ['The', 'FAA', 'began', 'restricting', 'passengers', '&apos;', 'use', 'of', 'electronic', 'devices', 'in', '1966', 'in', 'response', 'to', 'reports', 'of', 'interference', 'with', 'navigation', 'and', 'communications', 'equipment', 'when', 'passengers', 'began', 'carrying', 'FM', 'radios', ',', 'the', 'high-tech', 'gadgets', 'of', 'their', 'day', '.']
> PRED 2722: Der Hotel des Europäischen Parlaments wird die Kommission des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -56.6344
>
> [2021-10-12 22:22:44,302 INFO]
> SENT 2723: ['New', 'airliners', 'are', 'far', 'more', 'reliant', 'on', 'electrical', 'systems', 'than', 'previous', 'generations', 'of', 'aircraft', ',', 'but', 'they', 'are', 'also', 'designed', 'and', 'approved', 'by', 'the', 'FAA', 'to', 'be', 'resistant', 'to', 'electronic', 'interference', '.']
> PRED 2723: Mit der Nähe des Europäischen Parlaments kann die Nähe des Europäischen Parlaments des Europäischen Parlaments in der Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -57.6100
>
> [2021-10-12 22:22:44,302 INFO]
> SENT 2724: ['Airlines', 'have', 'been', 'offering', 'Wi-Fi', 'use', 'at', 'cruising', 'altitudes', 'to', 'passengers', 'for', 'several', 'years', '.']
> PRED 2724: Im Namen des Europäischen Parlaments hat die Frage des Europäischen Parlaments .
> PRED SCORE: -25.9868
>
> [2021-10-12 22:22:44,302 INFO]
> SENT 2725: ['Planes', 'modified', 'for', 'Wi-Fi', 'systems', 'are', 'also', 'more', 'resistant', 'to', 'interference', '.']
> PRED 2725: Mit der EU des Europäischen Parlaments wird die Möglichkeit .
> PRED SCORE: -25.0149
>
> [2021-10-12 22:22:44,303 INFO]
> SENT 2726: ['The', 'vast', 'majority', 'of', 'airliners', 'should', 'qualify', 'for', 'greater', 'electronic', 'device', 'use', 'under', 'the', 'new', 'guidelines', ',', 'Huerta', 'said', '.']
> PRED 2726: Der Hotel des Europäischen Parlaments wird die Regierung des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -36.9952
>
> [2021-10-12 22:22:44,303 INFO]
> SENT 2727: ['Today', '&apos;s', 'electronic', 'devices', 'generally', 'emit', 'much', 'lower', 'power', 'radio', 'transmissions', 'than', 'previous', 'generations', 'of', 'devices', '.']
> PRED 2727: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -32.4938
>
> [2021-10-12 22:22:44,303 INFO]
> SENT 2728: ['E-readers', ',', 'for', 'example', ',', 'emit', 'only', 'minimal', 'transmissions', 'when', 'turning', 'a', 'page', '.']
> PRED 2728: Im Rahmen des Europäischen Parlaments wird die Frage des Europäischen Parlaments .
> PRED SCORE: -26.7050
>
> [2021-10-12 22:22:44,303 INFO]
> SENT 2729: ['But', 'transmissions', 'are', 'stronger', 'when', 'devices', 'are', 'downloading', 'or', 'sending', 'data', '.']
> PRED 2729: Es ist die Frage des Europäischen Parlaments .
> PRED SCORE: -17.8453
>
> [2021-10-12 22:22:44,303 INFO]
> SENT 2730: ['Among', 'those', 'pressing', 'for', 'a', 'relaxation', 'of', 'restrictions', 'on', 'passengers', '&apos;', 'use', 'of', 'the', 'devices', 'has', 'been', 'Amazon.com.']
> PRED 2730: Mit der EU des Europäischen Parlaments wird die Möglichkeit des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -34.8560
>
> [2021-10-12 22:22:44,380 INFO]
> SENT 2731: ['In', '2011', ',', 'company', 'officials', 'loaded', 'an', 'airliner', 'full', 'of', 'their', 'Kindle', 'e-readers', 'and', 'flew', 'it', 'around', 'to', 'test', 'for', 'problems', 'but', 'found', 'none', '.']
> PRED 2731: Im Rahmen des Europäischen Parlaments werden die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -38.9560
>
> [2021-10-12 22:22:44,380 INFO]
> SENT 2732: ['FAA', 'advisory', 'committee', 'members', 'expressed', 'mixed', 'feelings', 'about', 'whether', 'use', 'of', 'the', 'devices', 'presents', 'any', 'risk', '.']
> PRED 2732: Mit der EU des Europäischen Parlaments wird die Möglichkeit des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -36.5212
>
> [2021-10-12 22:22:44,380 INFO]
> SENT 2733: ['Douglas', 'Kidd', 'of', 'the', 'National', 'Association', 'of', 'Airline', 'Passengers', 'said', 'he', 'believes', 'interference', 'from', 'the', 'devices', 'is', 'genuine', 'even', 'if', 'the', 'risk', 'is', 'minimal', '.']
> PRED 2733: Mit der EU des Europäischen Parlaments wird die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -44.9822
>
> [2021-10-12 22:22:44,380 INFO]
> SENT 2734: ['Other', 'committee', 'members', 'said', 'there', 'are', 'only', 'anecdotal', 'reports', 'from', 'pilots', 'to', 'support', 'that', 'the', 'devices', 'can', 'interfere', 'with', 'aircraft', 'systems', ',', 'and', 'most', 'of', 'those', 'reports', 'are', 'very', 'old', '.']
> PRED 2734: Mit der EU des Europäischen Parlaments werden sich die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -54.0179
>
> [2021-10-12 22:22:44,381 INFO]
> SENT 2735: ['However', ',', 'the', 'committee', 'recommended', 'the', 'FAA', 'allow', 'pilots', 'to', 'order', 'passengers', 'to', 'shut', 'off', 'devices', 'during', 'instrument', 'landings', 'in', 'low', 'visibility', '.']
> PRED 2735: Es ist die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -32.7331
>
> [2021-10-12 22:22:44,381 INFO]
> SENT 2736: ['A', 'travel', 'industry', 'group', 'welcomed', 'the', 'changes', ',', 'calling', 'them', 'common-sense', 'accommodations', 'for', 'a', 'traveling', 'public', 'now', 'bristling', 'with', 'technology', '.']
> PRED 2736: Mit der Nähe des Europäischen Parlaments wird die Möglichkeit des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -42.0419
>
> [2021-10-12 22:22:44,381 INFO]
> SENT 2737: ['&quot;', 'We', '&apos;re', 'pleased', 'the', 'FAA', 'recognizes', 'that', 'an', 'enjoyable', 'passenger', 'experience', 'is', 'not', 'incompatible', 'with', 'safety', 'and', 'security', ',', '&quot;', 'said', 'Roger', 'Dow', ',', 'CEO', 'of', 'the', 'U.S.', 'Travel', 'Association', '.']
> PRED 2737: Wir haben die Frage des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments des Europäischen Parlaments .
> PRED SCORE: -48.3777
