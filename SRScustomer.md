Project Name: FitMatch 
Team: Jace Tillman - Provider, Cali Hunt - Customer 
Course: CSC 340 
Version: 1.0 
Date: 2026-09-16

1. Overview

A service provider app created for local home cooks / caterers offering meal plans and hot meal drop offs. The app aims to help busy families, and professionals save time on cooking while also having tasty nutritious meals.

Glossary:

Cook: The home cook/caterer providing meals to customers
Customer: A person looking to buy meals
Profile: A collection of information about a user
Services: The specific meal/meal prep provided by a cook

Primary Users and Roles:

Customer - Find cooks aligned with personal tastes/dietary preferences
Cook - Attract clients and manage services

Scope:

User Profiles for customers and cooks
Search and browse cooks by meals
Ordering meals and meal prep subscriptions
Reviews and ratings
Delivery scheduling

Out of Scope:

Macro customization
Delivery tracker

2. Functional Requirements

2.1: Customer Stories

US-1 - Register an account

Scenario: Register with valid credentials
Given I am not registered
When I provide valid registration details
Then I should be successfully registered and logged in and can view my profile

US-2 - View cooks by cuisine and dietary preferences

Scenario: View cooks by cuisine and dietary preferences
Given I am logged in as a customer
When I select the cuisine and dietary preferences
Then I should see a list of cooks who specialize in that category


US-3 - Order one time meals or subscribe to weekly meal plan

Scenario: Order meal/meal plan subscription
Given I am logged in as a customer
When I select a meal/meal plan subscription
Then I should receive confirmation of the order and view it on my dashboard


US-4 - Leave ratings and reviews

Scenario: Write a review after ordering
Given I have received the ordered food
When I submit a rating and review for that meal
Then the review should be saved and visible to other customers


2.2: Provider (Cook) Stories

US-5 -







US-6 - 







US-7 - 






US-8 - 






3. Non-Functional Requirements

Performance: Pages should be loaded in a reasonable time, common actions should respond promptly
Availability: The system should handle errors, logouts, and app closes without losing information, with maintenance windows communicated in advance
Security: All password encrypted, 2FA is required, users and cooks can only modify their own accounts
Usability: Simple and understandable UI for both users and cooks, users should easily be able to find profiles, meals, orders, and reviews


4. Assumptions, Constraints, and Policies

Assumptions: 
Constraints: 
Policies: 


5. Milestones

M1 Requirements — this file and related stories opened as issues. 
M2 High-fidelity prototype — core customer and provider UI flows are fully interactive. 
M3 Design — architecture, schema, and API outline. 
M4 Backend API — key endpoints and tests. 
M5 Increment — at least 2 use cases end-to-end.

M6 Final — complete system and documentation.


6. Change Management





