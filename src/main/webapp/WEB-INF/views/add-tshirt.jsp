<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>Add T-Shirt</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/styles.css">
</head>
<body>
	<div class="container">

		<h1>Add New T-Shirt</h1>
		<c:if test="${not empty error}">
			<div class="alert alert-error">${error}</div>
		</c:if>

		<form:form method="post" modelAttribute="tShirt">

			<div class="form-group">
				<form:label path="tShirtId">T-Shirt ID:</form:label>
				<form:input path="tShirtId" cssClass="form-control" />
			</div>

			<div class="form-group">
				<form:label path="name">Name:</form:label>
				<form:input path="name" cssClass="form-control" />
			</div>

			<div class="form-group">
				<form:label path="colour">Colour:</form:label>
				<form:input path="colour" cssClass="form-control" />
			</div>

			<div class="form-group">
				<form:label path="genderRecommendation">Gender:</form:label>
				<form:select path="genderRecommendation" cssClass="form-control">
					<form:options items="${genders}" />
				</form:select>
			</div>

			<div class="form-group">
				<form:label path="size">Size:</form:label>
				<form:select path="size" cssClass="form-control">
					<form:options items="${sizes}" />
				</form:select>
			</div>

			<div class="form-group">
				<form:label path="price">Price:</form:label>
				<form:input path="price" cssClass="form-control" />
			</div>

			<div class="form-group">
				<form:label path="rating">Rating:</form:label>
				<form:input path="rating" cssClass="form-control" />
			</div>

			<div class="form-group">
				<form:label path="availability">Availability:</form:label>
				<form:checkbox path="availability" />
			</div>

			<button type="submit" class="btn">Add T-Shirt</button>

		</form:form>


		<a href="/tshirts/search" class="action-link">Back to Search</a>
	</div>
</body>
</html>