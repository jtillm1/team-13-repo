<#import "layout.ftlh" as layout>
<@layout.page title="Home">

  <section class="hero page-section">
    <div class="container">
      <h1>MealShare</h1>
      <p>Order home-cooked meals and meal plans from local cooks who match your cuisine and dietary preferences.</p>
      <div style="display:flex; gap:12px; margin-top:20px; flex-wrap:wrap;">
        <#if customerId??>
          <a href="/browse-cooks" class="btn btn-accent">Browse Cooks</a>
        <#else>
          <a href="/customers/new" class="btn btn-accent">Sign Up as a Customer</a>
          <a href="/customers/login" class="btn btn-outline-light">Log In</a>
        </#if>
      </div>
    </div>
  </section>

  <section class="page-section tight">
    <div class="container">
      <div class="section-heading">
        <h2>Meet a few of our cooks</h2>
        <p>Home cooks and caterers near you, rated by real customers.</p>
      </div>
      <#if featuredCooks?size == 0>
        <p>No cooks have joined yet.</p>
      <#else>
        <div class="grid grid-3">
          <#list featuredCooks as cook>
            <@layout.cookCard cook=cook />
          </#list>
        </div>
      </#if>
      <p style="margin-top:16px;"><a href="/browse-cooks">Browse all cooks &rarr;</a></p>
    </div>
  </section>

</@layout.page>