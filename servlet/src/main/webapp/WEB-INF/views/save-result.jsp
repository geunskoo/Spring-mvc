<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>id=<%=((Member)reqeust.getAttribute("member")).getId()%></li>
    <li>username=<%=((Member)reqeust.getAttribute("member")).getUsername()%></li>
    <li>age=<%=((Member)reqeust.getAttribute("member")).getAge()%></li>

</ul>
<a href="/index.html">메인</a>
</body>
</html>