<#import "layout.ftlh" as layout>
<@layout.page title=editing?then("Settings", "Sign Up")>

  <section class="hero page-section tight">
    <div class="container">
      <h1>${editing?then("Settings", "Create your account")}</h1>
      <p>${editing?then("Update your info and preferences.", "Tell us a bit about your taste so we can match you with the right home cooks.")}</p>
    </div>
  </section>

  <section class="page-section">
    <div class="container">
      <div class="card form-card">

        <form method="post" action="${formAction}" novalidate>

          <div class="form-group">
            <label for="firstName">First name</label>
            <input type="text" id="firstName" name="firstName" value="${customer.firstName!''}">
            <#if errors.firstName??><p class="field-error">${errors.firstName}</p></#if>
          </div>

          <div class="form-group">
            <label for="lastName">Last name</label>
            <input type="text" id="lastName" name="lastName" value="${customer.lastName!''}">
            <#if errors.lastName??><p class="field-error">${errors.lastName}</p></#if>
          </div>

          <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="${customer.email!''}">
            <#if errors.email??><p class="field-error">${errors.email}</p></#if>
          </div>

          <div class="form-group">
            <label for="password"><#if editing>New password (leave blank to keep current)<#else>Password</#if></label>
            <input type="password" id="password" name="password">
            <#if errors.password??><p class="field-error">${errors.password}</p></#if>
          </div>

          <#if !editing>
            <div class="form-group">
              <label for="confirmPassword">Confirm password</label>
              <input type="password" id="confirmPassword" name="confirmPassword">
              <#if errors.confirmPassword??><p class="field-error">${errors.confirmPassword}</p></#if>
            </div>
          </#if>

          <div class="form-group">
            <label>Dietary preferences</label>
            <div class="checkbox-row">
              <#list diets as d>
                <label class="checkbox-pill">
                  <input type="checkbox" name="dietaryPreferences" value="${d}"
                    <#if (customer.dietaryPreferences![])?seq_contains(d)>checked</#if>> ${d}
                </label>
              </#list>
            </div>
          </div>

          <div class="form-group">
            <label>Cuisine preferences</label>
            <div class="checkbox-row">
              <#list cuisines as c>
                <label class="checkbox-pill">
                  <input type="checkbox" name="cuisinePreferences" value="${c}"
                    <#if (customer.cuisinePreferences![])?seq_contains(c)>checked</#if>> ${c}
                </label>
              </#list>
            </div>
          </div>

          <button type="submit" class="btn btn-primary btn-block">${editing?then("Save Changes", "Create account")}</button>
        </form>

        <#if editing>
          <form method="post" action="/customers/${customerId?c}/delete" style="margin-top:12px;"
                onsubmit="return confirm('Delete your account? This cannot be undone.');">
            <button type="submit" class="btn btn-danger btn-block">Delete Account</button>
          </form>
        <#else>
          <p class="form-footer-text">Already have an account? <a href="/customers/login">Log in</a></p>
        </#if>

      </div>
    </div>
  </section>

</@layout.page>