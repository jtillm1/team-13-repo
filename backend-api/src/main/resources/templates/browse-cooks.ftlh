<#import "layout.ftlh" as layout>
<@layout.page title="Browse Cooks">

  <section class="page-section tight">
    <div class="container">
      <div class="section-heading">
        <h1>Find a cook</h1>
        <p>Search by name, or filter by cuisine and dietary preference to find your match.</p>
      </div>

      <form method="get" action="/browse-cooks" class="filter-bar">
        <div class="form-group">
          <label for="name">Search</label>
          <input type="text" id="name" name="name" value="${name!''}" placeholder="Search cooks...">
        </div>

        <div class="form-group">
          <label for="cuisine">Cuisine</label>
          <select id="cuisine" name="cuisine">
            <option value="">All cuisines</option>
            <#list cuisines as c>
              <option value="${c}" <#if (cuisine!'') == c>selected</#if>>${c}</option>
            </#list>
          </select>
        </div>

        <div class="form-group">
          <label for="diet">Dietary preference</label>
          <select id="diet" name="diet">
            <option value="">All diets</option>
            <#list diets as d>
              <option value="${d}" <#if (diet!'') == d>selected</#if>>${d}</option>
            </#list>
          </select>
        </div>

        <button type="submit" class="btn btn-primary btn-small">Search</button>
        <a href="/browse-cooks" class="btn btn-outline btn-small">Clear</a>
      </form>

      <#if cooks?size == 0>
        <p style="color:var(--color-text-muted);">No cooks match those filters. Try clearing a filter.</p>
      <#else>
        <div class="grid grid-3">
          <#list cooks as cook>
            <@layout.cookCard cook=cook />
          </#list>
        </div>
      </#if>
    </div>
  </section>

</@layout.page>