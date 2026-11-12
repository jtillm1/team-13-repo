<#import "layout.ftlh" as layout>
<@layout.page title="Log In">

  <section class="hero page-section tight">
    <div class="container">
      <h1>Welcome back</h1>
      <p>Log in to browse cooks and manage your profile.</p>
    </div>
  </section>

  <section class="page-section">
    <div class="container">
      <div class="card form-card">
        <form method="post" action="/customers/login" novalidate>
          <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="${email!''}">
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password">
            <#if loginError??><p class="field-error">${loginError}</p></#if>
          </div>
          <button type="submit" class="btn btn-primary btn-block">Log in</button>
        </form>
        <p class="form-footer-text">New to MealShare? <a href="/customers/new">Create an account</a></p>
      </div>
    </div>
  </section>

</@layout.page>