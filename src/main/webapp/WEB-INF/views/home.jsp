<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home - T-Shirt Store</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
</head>
<body>

    <header>
        <div class="container">
            <h1>Welcome to the T-Shirt Store</h1>
        </div>
    </header>

    <div class="container">
        <div class="form-container" style="text-align: center;">
            
            <div style="margin-top: 30px;">
                <a href="<c:url value='/tshirts/add' />">
                    <button class="btn" style="margin-right: 20px;">Add a T-Shirt</button>
                </a>

                <a href="<c:url value='/tshirts/search' />">
                    <button class="btn">Search T-Shirts</button>
                </a>
            </div>
        </div>
    </div>

</body>
</html>
