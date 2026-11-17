// provider.js
// shared helper functions for the mealshare provider side.
// providers are the home cooks or caterers using the platform.
// this prototype uses localstorage instead of a real backend.

// these constants hold the localstorage names used throughout the file
const PROVIDERS_KEY = "mealshare_providers";
const CURRENT_PROVIDER_KEY = "mealshare_currentProviderId";
const MENU_ITEMS_KEY = "mealshare_menuItems";
const REVIEWS_KEY = "mealshare_reviews";
const ORDERS_KEY = "mealshare_orders";

/* ---------------- providers / authentication ---------------- */

// gets all provider accounts saved in localstorage
function getProviders() {
  return JSON.parse(localStorage.getItem(PROVIDERS_KEY)) || [];
}

// saves the provider list back into localstorage
function saveProviders(providers) {
  localStorage.setItem(PROVIDERS_KEY, JSON.stringify(providers));
}

// gets the provider who is currently signed in
function getCurrentProvider() {
  const id = localStorage.getItem(CURRENT_PROVIDER_KEY);

  // if there is no saved provider id, nobody is signed in
  if (!id) return null;

  // searches the provider list for the matching id
  return getProviders().find(function (p) {
    return p.id === id;
  }) || null;
}

// saves the id of the provider who just signed in
function setCurrentProvider(id) {
  localStorage.setItem(CURRENT_PROVIDER_KEY, id);
}

// logs the provider out by removing their saved session id
function logoutProvider() {
  localStorage.removeItem(CURRENT_PROVIDER_KEY);
}

// creates a new provider account
// returns the id assigned to the new provider
function registerProvider(provider) {
  const providers = getProviders();

  // creates a unique id using the current timestamp
  provider.id = "p_" + Date.now();

  // new providers must complete their profile later
  provider.profileComplete = false;

  // adds the new provider to the existing list
  providers.push(provider);

  // saves the updated list
  saveProviders(providers);

  // automatically signs in the new provider
  setCurrentProvider(provider.id);

  return provider.id;
}

// searches for a provider using their email address
function findProviderByEmail(email) {
  return getProviders().find(function (p) {
    // ignores uppercase and lowercase differences in email addresses
    return p.email.toLowerCase() === email.toLowerCase();
  }) || null;
}

// updates the provider's catering profile (US-5)
function updateProvider(id, updates) {
  const providers = getProviders();

  // finds the position of the provider in the array
  const idx = providers.findIndex(function (p) {
    return p.id === id;
  });

  // returns null if no provider with that id exists
  if (idx === -1) return null;

  // combines the old provider information with the new information
  // also marks the profile as complete
  providers[idx] = Object.assign(
    {},
    providers[idx],
    updates,
    { profileComplete: true }
  );

  // saves the updated provider list
  saveProviders(providers);

  return providers[idx];
}

// checks whether a provider is signed in before allowing access to a page
function requireProviderAuth() {
  const provider = getCurrentProvider();

  // sends the user to the login page if nobody is signed in
  if (!provider) {
    window.location.href = "login.html";
    return null;
  }

  return provider;
}

/* ---------------- menu items + pricing (US-6) ---------------- */

// gets every menu item saved in localstorage
function getAllMenuItems() {
  return JSON.parse(localStorage.getItem(MENU_ITEMS_KEY)) || [];
}

// saves the complete menu item list
function saveAllMenuItems(items) {
  localStorage.setItem(MENU_ITEMS_KEY, JSON.stringify(items));
}

// gets only the menu items belonging to one provider
function getMenuItems(providerId) {
  return getAllMenuItems().filter(function (m) {
    return m.providerId === providerId;
  });
}

// adds a new menu item
function addMenuItem(item) {
  const items = getAllMenuItems();

  // assigns a unique id to the new menu item
  item.id = "m_" + Date.now();

  // adds the item to the list
  items.push(item);

  // saves the updated list
  saveAllMenuItems(items);

  return item;
}

// updates an existing menu item
function updateMenuItem(id, updates) {
  const items = getAllMenuItems();

  // finds the menu item that matches the given id
  const idx = items.findIndex(function (m) {
    return m.id === id;
  });

  // returns null if the item does not exist
  if (idx === -1) return null;

  // combines the existing item with the updated information
  items[idx] = Object.assign({}, items[idx], updates);

  // saves the updated menu list
  saveAllMenuItems(items);

  return items[idx];
}

// deletes a menu item using its id
function deleteMenuItem(id) {
  // keeps every menu item except the one being deleted
  saveAllMenuItems(
    getAllMenuItems().filter(function (m) {
      return m.id !== id;
    })
  );
}

/* ---------------- reviews (US-7) ---------------- */

// gets every review saved in localstorage
function getAllReviews() {
  return JSON.parse(localStorage.getItem(REVIEWS_KEY)) || [];
}

// saves the complete review list
function saveAllReviews(reviews) {
  localStorage.setItem(REVIEWS_KEY, JSON.stringify(reviews));
}

// gets only the reviews belonging to one provider
function getReviews(providerId) {
  return getAllReviews().filter(function (r) {
    return r.providerId === providerId;
  });
}

// lets a provider respond to a customer review
function replyToReview(reviewId, replyText) {
  const reviews = getAllReviews();

  // finds the review that the provider wants to reply to
  const idx = reviews.findIndex(function (r) {
    return r.id === reviewId;
  });

  // returns null if the review cannot be found
  if (idx === -1) return null;

  // saves the provider's response
  reviews[idx].reply = replyText;

  // records the date the response was submitted
  reviews[idx].replyDate = new Date().toISOString().slice(0, 10);

  // saves the updated reviews
  saveAllReviews(reviews);

  return reviews[idx];
}

/* ---------------- orders + payment status (US-8) ---------------- */

// gets every order saved in localstorage
function getAllOrders() {
  return JSON.parse(localStorage.getItem(ORDERS_KEY)) || [];
}

// saves the complete order list
function saveAllOrders(orders) {
  localStorage.setItem(ORDERS_KEY, JSON.stringify(orders));
}

// gets only the orders belonging to one provider
function getOrders(providerId) {
  return getAllOrders().filter(function (o) {
    return o.providerId === providerId;
  });
}

/* ---------------- demo data ---------------- */

// the customer side of the prototype does not create real orders or reviews yet.
// these sample records give the provider dashboard something to display.
// this happens the first time a provider visits their dashboard.

// adds sample reviews and orders if the provider does not already have any
function seedDemoDataIfNeeded(providerId) {
  const reviews = getAllReviews();

  // checks whether this provider already has reviews
  if (!reviews.some(function (r) {
    return r.providerId === providerId;
  })) {

    // adds two sample reviews for this provider
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

  // checks whether this provider already has orders
  if (!orders.some(function (o) {
    return o.providerId === providerId;
  })) {

    // adds two sample orders for this provider
    saveAllOrders(orders.concat([
      {
        id: "o_" + Date.now(),
        providerId: providerId,
        customerName: "Alexis P.",
        items: [
          {
            name: "Mediterranean Bowl",
            qty: 2,
            price: 12.5
          }
        ],
        total: 25.0,
        status: "Paid",
        date: "2026-09-18"
      },
      {
        id: "o_" + (Date.now() + 1),
        providerId: providerId,
        customerName: "Marcus T.",
        items: [
          {
            name: "Weekly Family Pack",
            qty: 1,
            price: 48.0
          }
        ],
        total: 48.0,
        status: "Unpaid",
        date: "2026-09-20"
      }
    ]));
  }
}