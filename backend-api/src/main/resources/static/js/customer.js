/*
 * mealshare - customer js
 * this file stores sample cook and meal information.
 * it also has helper functions that read and write information
 * using localstorage, so the prototype can work without a backend.
 */

// sample cook profiles and their meals
const COOKS = [
  {
    id: "maria",
    name: "Maria's Kitchen",
    cuisine: "Italian",
    diet: ["Vegetarian", "Gluten-Free"],
    rating: 4.8,
    image: "maria.jpg",
    bio: "Home-style Italian cooking made with fresh, seasonal ingredients. Family recipes passed down three generations.",
    meals: [
      {
        id: "lasagna",
        name: "Lasagna",
        price: 14,
        image: "lasagna.jpg",
        desc: "Layers of pasta, ricotta, and slow-simmered tomato sauce."
      },
      {
        id: "primavera",
        name: "Pasta Primavera",
        price: 12,
        image: "pasta-primavera.jpg",
        desc: "Fresh vegetables tossed with pasta in a light garlic sauce."
      },
      {
        id: "parm",
        name: "Chicken Parmesan",
        price: 15,
        image: "chicken-parmesan.jpg",
        desc: "Breaded chicken breast, marinara, and melted mozzarella."
      }
    ]
  },
  {
    id: "james",
    name: "James' Meal Prep",
    cuisine: "American",
    diet: ["High-Protein", "Dairy-Free"],
    rating: 4.7,
    image: "james.jpg",
    bio: "Weekly meal prep built for people who train hard. Balanced macros, no fuss.",
    meals: [
      {
        id: "chicken-rice",
        name: "Grilled Chicken & Rice Bowl",
        price: 11,
        image: "chicken-rice-bowl.jpg",
        desc: "Grilled chicken breast, brown rice, and steamed broccoli."
      },
      {
        id: "turkey-bowl",
        name: "Turkey Taco Bowl",
        price: 12,
        image: "turkey-taco-bowl.jpg",
        desc: "Seasoned ground turkey, peppers, and cilantro-lime rice."
      }
    ]
  },
  {
    id: "sofia",
    name: "Sofia's Cocina",
    cuisine: "Mexican",
    diet: ["Vegetarian"],
    rating: 4.9,
    image: "sofia.jpg",
    bio: "Bright, veggie-forward Mexican dishes made from scratch every morning.",
    meals: [
      {
        id: "veg-enchiladas",
        name: "Veggie Enchiladas",
        price: 13,
        image: "veggie-enchiladas.jpg",
        desc: "Corn tortillas filled with roasted vegetables and cheese."
      },
      {
        id: "bean-tacos",
        name: "Black Bean Tacos",
        price: 10,
        image: "black-bean-tacos.jpg",
        desc: "Soft tacos with seasoned black beans and pico de gallo."
      }
    ]
  },
  {
    id: "hana",
    name: "Hana's Kitchen",
    cuisine: "Asian",
    diet: ["Vegan", "Gluten-Free"],
    rating: 4.8,
    image: "hana.jpg",
    bio: "Plant-based Asian comfort food, made gluten-free without losing any flavor.",
    meals: [
      {
        id: "veg-stirfry",
        name: "Vegetable Stir-Fry",
        price: 12,
        image: "veggie-stirfry.jpg",
        desc: "Seasonal vegetables tossed in a gluten-free tamari sauce over rice."
      },
      {
        id: "tofu-curry",
        name: "Tofu Curry",
        price: 13,
        image: "tofu-curry.jpg",
        desc: "Crispy tofu simmered in a coconut curry sauce."
      }
    ]
  }
];

// sample reviews so the reviews section has content on the first visit
const SEED_REVIEWS = [
  {
    cookId: "maria",
    meal: "Lasagna",
    stars: 5,
    text: "Best lasagna I've had delivered, tasted homemade!",
    reviewer: "Demo Customer"
  },
  {
    cookId: "sofia",
    meal: "Veggie Enchiladas",
    stars: 5,
    text: "So fresh and flavorful, will order again.",
    reviewer: "Demo Customer"
  }
];

// sample order that is already delivered
// this gives the review feature (US-4) an order to work with
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

// gets the currently saved customer from localstorage
function getUser() {
  return JSON.parse(localStorage.getItem("mealshare_user") || "null");
}

// saves customer information in localstorage
function saveUser(user) {
  localStorage.setItem("mealshare_user", JSON.stringify(user));
}

// gets saved orders from localstorage
function getOrders() {
  const stored = JSON.parse(localStorage.getItem("mealshare_orders") || "null");

  // if this is the first visit, save the sample order
  if (!stored) {
    localStorage.setItem("mealshare_orders", JSON.stringify(SEED_ORDERS));
    return SEED_ORDERS;
  }

  return stored;
}

// saves an updated list of orders
function saveOrders(orders) {
  localStorage.setItem("mealshare_orders", JSON.stringify(orders));
}

// gets saved reviews from localstorage
function getReviews() {
  const stored = JSON.parse(localStorage.getItem("mealshare_reviews") || "null");

  // if there are no saved reviews yet, add the sample reviews
  if (!stored) {
    localStorage.setItem("mealshare_reviews", JSON.stringify(SEED_REVIEWS));
    return SEED_REVIEWS;
  }

  return stored;
}

// saves an updated list of reviews
function saveReviews(reviews) {
  localStorage.setItem("mealshare_reviews", JSON.stringify(reviews));
}

// finds a cook using their unique id
function findCook(cookId) {
  return COOKS.find(c => c.id === cookId);
}

// finds a specific meal belonging to a cook
function findMeal(cookId, mealId) {
  const cook = findCook(cookId);

  // if the cook does not exist, there cannot be a matching meal
  if (!cook) return null;

  return cook.meals.find(m => m.id === mealId);
}

// gets a value from the current page's url
// example: ?cook=maria
function getQueryParam(name) {
  const params = new URLSearchParams(window.location.search);
  return params.get(name);
}

// updates the navigation bar with the customer's name
// if nobody is signed in, it displays "Guest"
function updateNavGreeting() {
  const el = document.getElementById("nav-user-name");

  // stops if this page does not have the customer name element
  if (!el) return;

  const user = getUser();

  el.textContent = user ? user.firstName : "Guest";
}

// waits until the html page has loaded before updating the navbar
document.addEventListener("DOMContentLoaded", updateNavGreeting);