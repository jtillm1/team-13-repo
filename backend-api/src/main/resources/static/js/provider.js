// provider.js
// Shared helpers for the MealShare PROVIDER (home cook / caterer) side.
// There's no backend for this prototype, so everything is faked with
// localStorage - same approach as the customer side (see js/customer.js).

const PROVIDERS_KEY = "mealshare_providers";
const CURRENT_PROVIDER_KEY = "mealshare_currentProviderId";
const MENU_ITEMS_KEY = "mealshare_menuItems";
const REVIEWS_KEY = "mealshare_reviews";
const ORDERS_KEY = "mealshare_orders";

/* ---------------- Providers / auth ---------------- */

function getProviders() {
  return JSON.parse(localStorage.getItem(PROVIDERS_KEY)) || [];
}

function saveProviders(providers) {
  localStorage.setItem(PROVIDERS_KEY, JSON.stringify(providers));
}

function getCurrentProvider() {
  const id = localStorage.getItem(CURRENT_PROVIDER_KEY);
  if (!id) return null;
  return getProviders().find(function (p) { return p.id === id; }) || null;
}

function setCurrentProvider(id) {
  localStorage.setItem(CURRENT_PROVIDER_KEY, id);
}

function logoutProvider() {
  localStorage.removeItem(CURRENT_PROVIDER_KEY);
}

// Create a new provider account (business name / email / password).
// Returns the new provider's id.
function registerProvider(provider) {
  const providers = getProviders();
  provider.id = "p_" + Date.now();
  provider.profileComplete = false;
  providers.push(provider);
  saveProviders(providers);
  setCurrentProvider(provider.id);
  return provider.id;
}

function findProviderByEmail(email) {
  return getProviders().find(function (p) {
    return p.email.toLowerCase() === email.toLowerCase();
  }) || null;
}

// US-5: save/update the catering profile for the given provider id.
function updateProvider(id, updates) {
  const providers = getProviders();
  const idx = providers.findIndex(function (p) { return p.id === id; });
  if (idx === -1) return null;
  providers[idx] = Object.assign({}, providers[idx], updates, { profileComplete: true });
  saveProviders(providers);
  return providers[idx];
}

// Call this at the top of every protected provider page. Bounces back to
// login if nobody is signed in.
function requireProviderAuth() {
  const provider = getCurrentProvider();
  if (!provider) {
    window.location.href = "login.html";
    return null;
  }
  return provider;
}

/* ---------------- Menu items + pricing (US-6) ---------------- */

function getAllMenuItems() {
  return JSON.parse(localStorage.getItem(MENU_ITEMS_KEY)) || [];
}

function saveAllMenuItems(items) {
  localStorage.setItem(MENU_ITEMS_KEY, JSON.stringify(items));
}

function getMenuItems(providerId) {
  return getAllMenuItems().filter(function (m) { return m.providerId === providerId; });
}

function addMenuItem(item) {
  const items = getAllMenuItems();
  item.id = "m_" + Date.now();
  items.push(item);
  saveAllMenuItems(items);
  return item;
}

function updateMenuItem(id, updates) {
  const items = getAllMenuItems();
  const idx = items.findIndex(function (m) { return m.id === id; });
  if (idx === -1) return null;
  items[idx] = Object.assign({}, items[idx], updates);
  saveAllMenuItems(items);
  return items[idx];
}

function deleteMenuItem(id) {
  saveAllMenuItems(getAllMenuItems().filter(function (m) { return m.id !== id; }));
}

/* ---------------- Reviews (US-7) ---------------- */

function getAllReviews() {
  return JSON.parse(localStorage.getItem(REVIEWS_KEY)) || [];
}

function saveAllReviews(reviews) {
  localStorage.setItem(REVIEWS_KEY, JSON.stringify(reviews));
}

function getReviews(providerId) {
  return getAllReviews().filter(function (r) { return r.providerId === providerId; });
}

function replyToReview(reviewId, replyText) {
  const reviews = getAllReviews();
  const idx = reviews.findIndex(function (r) { return r.id === reviewId; });
  if (idx === -1) return null;
  reviews[idx].reply = replyText;
  reviews[idx].replyDate = new Date().toISOString().slice(0, 10);
  saveAllReviews(reviews);
  return reviews[idx];
}

/* ---------------- Orders + payment status (US-8) ---------------- */

function getAllOrders() {
  return JSON.parse(localStorage.getItem(ORDERS_KEY)) || [];
}

function saveAllOrders(orders) {
  localStorage.setItem(ORDERS_KEY, JSON.stringify(orders));
}

function getOrders(providerId) {
  return getAllOrders().filter(function (o) { return o.providerId === providerId; });
}

/* ---------------- Demo data ---------------- */
// The customer side of the prototype doesn't generate real orders/reviews
// yet, so we seed a couple of sample ones the first time a provider visits
// their dashboard - just so US-7 and US-8 have something to show.
function seedDemoDataIfNeeded(providerId) {
  const reviews = getAllReviews();
  if (!reviews.some(function (r) { return r.providerId === providerId; })) {
    saveAllReviews(reviews.concat([
      {
        id: "r_" + Date.now(),
        providerId: providerId,
        customerName: "Alexis P.",
        rating: 5,
        comment: "The Mediterranean bowls were incredible and arrived right on time!",
        date: "2026-09-10",
        reply: null
      },
      {
        id: "r_" + (Date.now() + 1),
        providerId: providerId,
        customerName: "Marcus T.",
        rating: 4,
        comment: "Really tasty, wish there were more vegetarian options.",
        date: "2026-09-15",
        reply: null
      }
    ]));
  }

  const orders = getAllOrders();
  if (!orders.some(function (o) { return o.providerId === providerId; })) {
    saveAllOrders(orders.concat([
      {
        id: "o_" + Date.now(),
        providerId: providerId,
        customerName: "Alexis P.",
        items: [{ name: "Mediterranean Bowl", qty: 2, price: 12.5 }],
        total: 25.0,
        status: "Paid",
        date: "2026-09-18"
      },
      {
        id: "o_" + (Date.now() + 1),
        providerId: providerId,
        customerName: "Marcus T.",
        items: [{ name: "Weekly Family Pack", qty: 1, price: 48.0 }],
        total: 48.0,
        status: "Unpaid",
        date: "2026-09-20"
      }
    ]));
  }
}