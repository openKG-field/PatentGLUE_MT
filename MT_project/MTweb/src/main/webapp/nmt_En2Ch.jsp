<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="charset" content="utf-8">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" href="css/clear-style.css">
    <link rel="stylesheet" href="css/nmt.css?v=1">
    <title>Machine Translation</title>
</head>
<body>
<div class="header-bar">
    <div class="header-left">
        <div class="logo">
            M - T
        </div>
        <div class="logo-desc">
            MT Platform
        </div>
    </div>
</div>
<div class="translate-app">
    <div class="translate-title">
        <div class="translate-desc-left">
            MT Tool
        </div>
        <a type="button" class="translate-bottom" href="nmt_En2Ch.jsp">Chara</a>
        <a type="button" class="translate-bottom" href="nmt_File2Chara.jsp">File</a>
    </div>
    <form action="mt?type=1" method="post">
        <div class="translate-area">
            <div class="translate-area-left">
                <div class="language-choose">
                    <div class="language-select">
                        English
                    </div>
                </div>
                <label>
                    <textarea class="input-box" name="userInput"><%Object userIntput = request.getAttribute("userInput");if (userIntput != null) {%><%=userIntput.toString()%><% } %></textarea>
                </label>
                <input type="submit" class="translate-bottom" value="TransL">

            </div>
            <div class="translate-area-right">
                <div class="language-choose">
                    <div class="language-select">
                        Chinese
                    </div>
                </div>
                <div class="input-box box-output">
                    <%Object nmtOutput = request.getAttribute("nmtOutput");
                       if (nmtOutput != null) {
                    %>
                       <%=nmtOutput.toString()%>
                    <% } %>
                </div>
                <a type="submit" class="translate-bottom" href="nmt_Ch2En.jsp">Switch</a>
            </div>
        </div>
    </form>
</div>
</body>
</html>
