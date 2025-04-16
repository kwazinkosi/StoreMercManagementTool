<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Search T-Shirts</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/styles.css">
</head>
<body>
	<header>
		<div class="container">
			<h1>Search T-Shirts</h1>
		</div>
	</header>

	<div class="container">
		<div class="form-container">
			<form:form method="post" modelAttribute="criteria">
				<div class="form-group">
					<form:label path="colour">Color:</form:label>
					<form:input path="colour" cssClass="form-control" />
				</div>

				<div class="form-group">
					<form:label path="size">Size:</form:label>
					<form:select path="size" cssClass="form-control">
						<form:option value="">Any</form:option>
						<form:options items="${sizes}" />
					</form:select>
				</div>

				<div class="form-group">
					<form:label path="gender">Gender:</form:label>
					<form:select path="gender" cssClass="form-control">
						<form:option value="">Any</form:option>
						<form:options items="${genders}" />
					</form:select>
				</div>

				<div class="form-group">
					<form:label path="priceRangeMinStr">Min Price:</form:label>
					<form:input path="priceRangeMinStr" cssClass="form-control"
						type="number" step="0.01" />
				</div>

				<div class="form-group">
					<form:label path="priceRangeMaxStr">Max Price:</form:label>
					<form:input path="priceRangeMaxStr" cssClass="form-control"
						type="number" step="0.01" />
				</div>
				<div class="form-group">
					<form:label path="outputPreference">Sort By:</form:label>
					<form:select path="outputPreference" cssClass="form-control">
						<form:options items="${sortOptions}" />
					</form:select>
				</div>

				<button type="submit" class="btn">Search</button>
			</form:form>
		</div>
	</div>
</body>
</html>