<#import "layout.ftlh" as layout>
<@layout.page title="Not Found">

  <section class="page-section">
    <div class="container">
      <div class="card confirm-box">
        <h1>Something went wrong</h1>
        <p>${errorMessage!"The page you were looking for doesn't exist."}</p>
        <p style="color:var(--color-text-muted);">Status ${(status!0)?c} ${error!''} for ${path!''}</p>
        <a href="/" class="btn btn-primary">Back to Home</a>
      </div>
    </div>
  </section>

</@layout.page>