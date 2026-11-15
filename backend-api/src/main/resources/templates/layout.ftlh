<#macro page title="MealShare">
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>${title} | MealShare</title>
  <link rel="stylesheet" href="/css/style.css" />
</head>
<body>

  <nav class="navbar">
    <div class="container">
      <a href="/" class="logo">Meal<span>Share</span></a>
      <ul class="nav-links">
        <li><a href="/browse-cooks">Browse Cooks</a></li>
        <#if customerId??>
          <li><a href="/customers/${customerId?c}">Dashboard</a></li>
          <li><a href="/customers/${customerId?c}/edit">Settings</a></li>
          <li><a href="/customers/logout">Log Out</a></li>
          <li class="pill">Hi, ${customerName}</li>
        <#else>
          <li><a href="/customers/login">Log In</a></li>
          <li><a href="/customers/new" class="pill">Sign Up</a></li>
        </#if>
      </ul>
    </div>
  </nav>

  <#if flash??>
    <div class="container"><p class="alert">${flash}</p></div>
  </#if>

  <#nested>

  <footer>
    <p>MealShare demo prototype - CSC 340</p>
  </footer>

</body>
</html>
</#macro>

<#macro cookCard cook>
  <a class="card preview-card" href="/browse-cooks/${cook.id?c}">
    <div class="photo-block chef">
      <#if cook.imageUrl?has_content>
        <img src="${cook.imageUrl}" alt="${cook.name!''}">
      <#else>
        <span style="color:#fff;">${(cook.name!'?')?substring(0, 1)}</span>
      </#if>
    </div>
    <h4>${cook.name!'Unnamed cook'}</h4>
    <span class="rating"><#if cook.rating??>&#9733; ${cook.rating?string("0.0")}<#else>New</#if></span>
    <div class="tag-row">
      <#if cook.cuisine?has_content><span class="tag">${cook.cuisine}</span></#if>
      <#list (cook.dietaryOptions)![] as d><span class="tag">${d}</span></#list>
    </div>
  </a>
</#macro>