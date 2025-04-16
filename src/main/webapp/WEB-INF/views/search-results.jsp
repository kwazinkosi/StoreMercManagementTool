<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Search Results</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>T-Shirts Sorted by ${sortBy}</h1>
        </div>
    </header>
    
    <div class="container">
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        
        <c:if test="${not empty results}">
            <table class="results-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Color</th>
                        <th>Size</th>
                        <th>Gender</th>
                        <th>Price</th>
                        <th>Rating</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${results}" var="tshirt">
                        <tr>
                            <td>${tshirt.name}</td>
                            <td>${tshirt.colour}</td>
                            <td>${tshirt.size}</td>
                            <td>${tshirt.genderRecommendation}</td>
                            <td>$${tshirt.price}</td>
                            <td>${tshirt.rating}/5</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        
        <c:if test="${empty results}">
            <div class="alert">No T-Shirts found matching your criteria.</div>
        </c:if>
        
        <a href="${pageContext.request.contextPath}/tshirts/search" class="action-link">New Search</a>
    </div>
</body>
</html>