/* 
   MealShare - Customer JS
   This file holds our fake "database" (hard-coded cooks and
   meals) plus small helper functions that read/write to
   localStorage so the prototype can simulate real behavior
   without a real backend.] */

// Hard-coded cook + meal data
const COOKS = [
  {
    id: "maria",
    name: "Maria's Kitchen",
    cuisine: "Italian",
    diet: ["Vegetarian", "Gluten-Free"],
    rating: 4.8,
    //emoji: "\uD83C\uDF5D", // used as a placeholder "photo" since there's no image backend
    bio: "Home-style Italian cooking made with fresh, seasonal ingredients. Family recipes passed down three generations.",
    meals: [
      { id: "lasagna", name: "Lasagna", price: 14, /*emoji: "\uD83C\uDF5D",*/ desc: "Layers of pasta, ricotta, and slow-simmered tomato sauce." },
      { id: "primavera", name: "Pasta Primavera", price: 12, emoji: "\uD83C\uDF5D", desc: "Fresh vegetables tossed with pasta in a light garlic sauce." },
      { id: "parm", name: "Chicken Parmesan", price: 15, emoji: "\uD83C\uDF5D", desc: "Breaded chicken breast, marinara, and melted mozzarella." }
    ]
  },
  {
    id: "james",
    name: "James' Meal Prep",
    cuisine: "American",
    diet: ["High-Protein", "Dairy-Free"],
    rating: 4.7,
    //emoji: "\uD83C\uDF57",
    bio: "Weekly meal prep built for people who train hard. Balanced macros, no fuss.",
    meals: [
      { id: "chicken-rice", name: "Grilled Chicken & Rice Bowl", price: 11, /*emoji: "\uD83C\uDF57",*/ desc: "Grilled chicken breast, brown rice, and steamed broccoli." },
      { id: "turkey-bowl", name: "Turkey Taco Bowl", price: 12, emoji: "\uD83C\uDF57", desc: "Seasoned ground turkey, peppers, and cilantro-lime rice." }
    ]
  },
  {
    id: "sofia",
    name: "Sofia's Cocina",
    cuisine: "Mexican",
    diet: ["Vegetarian"],
    rating: 4.9,
    //emoji: "\uD83C\uDF2E",
    bio: "Bright, veggie-forward Mexican dishes made from scratch every morning.",
    meals: [
      { id: "veg-enchiladas", name: "Veggie Enchiladas", price: 13, /*emoji: "\uD83C\uDF2E",*/ desc: "Corn tortillas filled with roasted vegetables and cheese." },
      { id: "bean-tacos", name: "Black Bean Tacos", price: 10, emoji: "\uD83C\uDF2E", desc: "Soft tacos with seasoned black beans and pico de gallo." }
    ]
  },
  {
    id: "hana",
    name: "Hana's Kitchen",
    cuisine: "Asian",
    diet: ["Vegan", "Gluten-Free"],
    rating: 4.8,
    // emoji: "\uD83C\uDF5C",
    bio: "Plant-based Asian comfort food, made gluten-free without losing any flavor.",
    meals: [
      { id: "veg-stirfry", name: "Vegetable Stir-Fry", price: 12, /*emoji: "\uD83C\uDF5C",*/ desc: "Seasonal vegetables tossed in a gluten-free tamari sauce over rice." },
      { id: "tofu-curry", name: "Tofu Curry", price: 13, emoji: "\uD83C\uDF5C", desc: "Crispy tofu simmered in a coconut curry sauce." }
    ]
  }
];

// Seed reviews so the reviews section isn't empty on first load
const SEED_REVIEWS = [
  { cookId: "maria", meal: "Lasagna", stars: 5, text: "Best lasagna I've had delivered, tasted homemade!", reviewer: "Demo Customer" },
  { cookId: "sofia", meal: "Veggie Enchiladas", stars: 5, text: "So fresh and flavorful, will order again.", reviewer: "Demo Customer" }
];

// A seed order that's already "delivered" so the review flow
//      (US-4) has something to work with the first time the page loads 
const SEED_ORDERS = [
  {
    id: "order-seed-1",
    cookId: "james",
    cookName: "James' Meal Prep",
    mealName: "Grilled Chicken & Rice Bowl",
    orderType: "One-time order",
    quantity: 1,
    date: "2026-09-15",
    total: 11,
    status: "Delivered",
    reviewed: false
  }
];

// Small helpers for reading/writing localStorage
function getUser() {
  return JSON.parse(localStorage.getItem("mealshare_user") || "null");
}

function saveUser(user) {
  localStorage.setItem("mealshare_user", JSON.stringify(user));
}

function getOrders() {
  const stored = JSON.parse(localStorage.getItem("mealshare_orders") || "null");
  // First visit - plant the seed order so the dashboard isn't empty
  if (!stored) {
    localStorage.setItem("mealshare_orders", JSON.stringify(SEED_ORDERS));
    return SEED_ORDERS;
  }
  return stored;
}

function saveOrders(orders) {
  localStorage.setItem("mealshare_orders", JSON.stringify(orders));
}

function getReviews() {
  const stored = JSON.parse(localStorage.getItem("mealshare_reviews") || "null");
  // First time visiting, plant the seed reviews so the page has content
  if (!stored) {
    localStorage.setItem("mealshare_reviews", JSON.stringify(SEED_REVIEWS));
    return SEED_REVIEWS;
  }
  return stored;
}

function saveReviews(reviews) {
  localStorage.setItem("mealshare_reviews", JSON.stringify(reviews));
}

function findCook(cookId) {
  return COOKS.find(c => c.id === cookId);
}

function findMeal(cookId, mealId) {
  const cook = findCook(cookId);
  if (!cook) return null;
  return cook.meals.find(m => m.id === mealId);
}

// Reads a value out of the current page's URL, e.g. ?cook=maria
function getQueryParam(name) {
  const params = new URLSearchParams(window.location.search);
  return params.get(name);
}

// Updates the navbar to show the logged-in customer's name (or "Guest")
function updateNavGreeting() {
  const el = document.getElementById("nav-user-name");
  if (!el) return;
  const user = getUser();
  el.textContent = user ? user.firstName : "Guest";
}

document.addEventListener("DOMContentLoaded", updateNavGreeting);