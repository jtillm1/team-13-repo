<#import "layout.ftlh" as layout>
<@layout.page title="Dashboard">

  <section class="page-section tight">
    <div class="container">
      <div class="section-heading">
        <h1>My Dashboard</h1>
        <p>Your profile and food preferences.</p>
      </div>

      <div class="card">
        <div style="display:flex; justify-content:space-between; align-items:flex-start; flex-wrap:wrap; gap:12px;">
          <div>
            <h3>${customer.firstName} ${customer.lastName}</h3>
            <p style="color:var(--color-text-muted);">${customer.email}</p>
          </div>
          <a href="/customers/${customer.id?c}/edit" class="btn btn-outline btn-small">Settings</a>
        </div>

        <p style="margin-bottom:6px;"><strong>Dietary preferences</strong></p>
        <div class="tag-row" style="margin-bottom:16px;">
          <#list customer.dietaryPreferences as d>
            <span class="tag">${d}</span>
          <#else>
            <span style="color:var(--color-text-muted);">None selected</span>
          </#list>
        </div>

        <p style="margin-bottom:6px;"><strong>Cuisine preferences</strong></p>
        <div class="tag-row">
          <#list customer.cuisinePreferences as c>
            <span class="tag">${c}</span>
          <#else>
            <span style="color:var(--color-text-muted);">None selected</span>
          </#list>
        </div>
      </div>

      <a href="/browse-cooks" class="btn btn-primary" style="margin-top:20px;">Browse Cooks</a>
    </div>
  </section>

</@layout.page>