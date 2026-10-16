# StreetSmart — Beyond the Cash Box

A focused, mobile-first Android finance application designed for street vendors and small roadside shops in India, delivering intuitive cash reconciliation, income/expense tracking, unsold stock wastage monitoring, and weather-correlated sales insights.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following choices were confirmed during interactive clarification and have been built into the architecture:

- **Confirmed Decision 1 (Vendor Presets)**: Multi-vendor quick-entry presets for Tea Stalls, Vegetable/Fruit Carts, and Street Food Sellers (including pre-filled categories like Chai/Snacks, Fresh Produce, Packaging, Ice/Gas, Oil/Groceries).
- **Confirmed Decision 2 (Language Default)**: English default with an instant, persistent one-tap Tamil (`தமிழ்`) toggle in the top app bar for all labels, hints, and summary metrics.
- **Confirmed Decision 3 (Demonstration Data)**: Preloads the specified sample business day (Opening cash ₹500, Cash sales ₹2,000, UPI sales ₹1,000, Cash expenses ₹700, Closing cash ₹1,750 showing the ₹50 shortage warning) with prominent one-tap "Reset Sample Day" and "Clear All" controls.

---

## 1. Overview & Core Concept

- **Tagline**: *"Don’t just track what you earn. Understand what you keep."*
- **Problem Statement**: Small roadside vendors handle constant micro-cash and UPI flows while absorbing direct stock losses. At the end of a long day, physical cash in the drawer or box almost never matches mental math, hiding stock spoilage and cash leaks.
- **Target Audience**: Vegetable and fruit cart pushers, tea and snack stall owners, roadside tiffin stalls, and flower/chaat sellers across India.
- **Core Value**:
  1. **Dual-Channel Income**: Track cash sales and digital UPI collections separately without complex accounting terms.
  2. **Cash Reality Check**: Instant physical cash reconciliation (`Expected = Opening + Cash In - Cash Out`) showing exact discrepancies and actionable advice.
  3. **Stock & Spoilage Monitor**: Track purchased vs. sold quantities and calculate unsold stock and wastage losses.
  4. **Weather & Festival Correlation**: Understand how monsoon rains, heatwaves, or festival days affect street footfall and sales.
  5. **Guarded Savings Target**: Set daily/weekly savings goals while explicitly protecting vendors from mistaking cash flow for pure profit.

---

## 2. User Experience & Visual Design

### Visual Identity & Theme
- **Color Palette**:
  - Primary / Growth: Deep Emerald Green (`#0A6847` & `#108055`) conveying safety and earnings.
  - Surface & Background: Crisp Warm White (`#FBFDF9`) with Dark Charcoal (`#1A1C1E`) for sharp contrast under direct outdoor sunlight.
  - Cash Alert Shortage: Crimson Amber (`#BA1A1A` / `#FFDAD6`) for discrepancies.
  - Digital UPI Accent: Indigo Teal (`#1B4D3E` and `#006874`) for easy distinction from paper cash.
- **Typography & Accessibility**:
  - High-legibility numeric display with explicit Indian Rupee (`₹`) symbols everywhere.
  - Generous 48dp+ tap targets, high contrast, bold numeric cards, and icon badges designed for single-hand thumb navigation while standing at a cart.
- **Language Support (English + Tamil)**:
  - English: "Cash Sales", "UPI Sales", "Expenses", "Cash Reality Check", "Stock Wastage", "Estimated Profit".
  - Tamil: "ரொக்க விற்பனை", "UPI விற்பனை", "செலவுகள்", "பண சரிபார்ப்பு", "விற்பனையாகாத சரக்கு", "மதிப்பிடப்பட்ட லாபம்".

### Key User Flows & Screens

```
┌────────────────────────────────────────────────────────────────────────┐
│                     Top Bar: StreetSmart [EN | தமிழ்]                   │
├────────────────────────────────────────────────────────────────────────┤
│ ┌───────────────────────────┐  ┌────────────────────────────────────┐  │
│ │ Today: Total Sales ₹3,000 │  │ Reality Check: Shortage -₹50 ⚠️    │  │
│ │ Cash: ₹2,000 | UPI: ₹1,000│  │ Expected: ₹1,800 | Actual: ₹1,750  │  │
│ └───────────────────────────┘  └────────────────────────────────────┘  │
│                                                                        │
│ ┌──────────────────────────────────────────────────────────────────┐   │
│ │ Quick Actions: [+ Add Sale]  [- Add Expense]  [🔍 Cash Check]    │   │
│ └──────────────────────────────────────────────────────────────────┘   │
│                                                                        │
│ ┌───────────────────────────┐  ┌────────────────────────────────────┐  │
│ │ Stock Wastage: 5 kg loss  │  │ Weather Impact: 🌧️ Rainy (Sales:₹2k)│
│ └───────────────────────────┘  └────────────────────────────────────┘  │
│                                                                        │
│ ┌──────────────────────────────────────────────────────────────────┐   │
│ │ Savings Goal: ₹250 / ₹500 today [████████░░░░░] 50%              │   │
│ └──────────────────────────────────────────────────────────────────┘   │
├────────────────────────────────────────────────────────────────────────┤
│ Bottom Nav: [Dashboard]  [Sales]  [Expenses]  [Cash Check]  [Stock/More]│
└────────────────────────────────────────────────────────────────────────┘
```

1. **Dashboard (Home)**:
   - High-impact daily snapshot card: Total Sales, Cash vs. UPI breakdown, Total Expenses, Estimated Operating Profit (with disclaimer note).
   - Cash Reality Check mini-card highlighting balance status (Balanced, Surplus, or Shortage warning).
   - Date selector chip bar (Today, Yesterday, Custom Date).
   - Demo banner (when viewing sample day) with "Reset Sample Day" or "Start Fresh" buttons.
2. **Add / Edit Income**:
   - Quick numeric keypad input for Amount.
   - Payment method pill selector: **Cash** vs. **UPI (GPay / PhonePe / Paytm QR)**.
   - Preset chips for vendor types (e.g., Tea & Coffee, Snack Plate, Bulk Produce, Custom).
   - Optional note and timestamp.
3. **Add / Edit Expense**:
   - Amount input with direct cash payment flag.
   - Categorization: Wholesale Stock, Transport/Auto, Packaging/Bags, Stall Rent, Ice/Electricity, Gas/Fuel, Miscellaneous.
   - Quick date and vendor note.
4. **Cash Reality Check**:
   - Step-by-step cash counting assistant:
     - Opening Cash in box (₹)
     - (+) Cash Sales (auto-populated or manual override)
     - (+) Cash Added (personal deposit or loan)
     - (-) Cash Expenses paid from box
     - (-) Cash Withdrawn (owner personal draw or supplier payout)
     - (=) Expected Closing Cash
     - Actual Cash Counted in drawer
   - Real-time difference pill:
     - Negative (Shortage): Warning banner *"₹50 unaccounted for. Check for forgotten tea/snack expenses or wrong change given."*
     - Positive (Surplus): Notice *"₹X extra cash detected. Check for unrecorded cash sales."*
     - Zero (Match): Green checkmark *"Drawer balanced perfectly!"*
5. **Stock Tracker**:
   - Item entry: Item name, Unit (kg, pieces, bundles, crates, cups).
   - Quantity Purchased, Quantity Sold, Quantity Remaining.
   - Auto-computed Unsold Quantity and Spoilage/Wastage alert when leftover stock is damaged or discarded.
   - Practical restocking recommendations.
6. **Sales & Weather Comparison**:
   - Tagging options: Sunny ☀️, Rainy 🌧️, Cloudy ⛅, Festival / Market Day 🎪, Normal Day 🛒.
   - Visual comparison bars showing average sales by weather/day condition so vendors know how much stock to buy for rainy vs. festival days.
7. **Savings Goals & Summary**:
   - Daily / Weekly savings milestone setter (e.g., target ₹300/day towards vehicle loan or festival reserve).
   - Progress bar with caution warning: *"Estimated profit is an indicator, not guaranteed cash in hand."*
8. **Transaction History & Filter**:
   - Chronological list of all sales and expenses.
   - Filter by date, cash vs. UPI, and category. Edit/Delete with confirmation.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Local Room Database with Flow (Offline First)**
  - *Chosen Approach*: Android Room DB with entities for Transactions, Stock Items, Day Registers, and Savings.
  - *Why*: Street vendors operate on the road, often with spotty 4G data or limited battery. The app must launch instantly and persist every transaction offline without requiring a cloud login or remote server.
  - *Alternatives Considered*: SharedPreferences/DataStore only handles simple key-values and struggles with date filtering and stock histories. Cloud Firestore requires network connectivity and Google Sign-In setup which adds unnecessary barriers for roadside stall owners.
- **Decision 2: Instant English-Tamil Language Toggle**
  - *Chosen Approach*: An in-memory/DataStore supported language dictionary mapping key domain strings and helper advice, switchable at any moment from the top bar with a toggle chip.
  - *Why*: Many vendors in southern India (and specifically Tamil Nadu) conduct daily business in Tamil, but may also appreciate English numerals. Instant toggle without leaving the screen makes it friction-free.
- **Decision 3: Non-Destructive Demonstration Mode**
  - *Chosen Approach*: Seed the database on first run with the official demo scenario (₹500 opening, ₹2,000 cash sales, ₹1,000 UPI, ₹700 cash expense, ₹1,750 actual closing = -₹50 difference), marked with `isDemo = true`.
  - *Why*: Enables immediate evaluation while providing a clear "Clear Demo Data" button so vendors can start recording their real business records anytime.

---

## 4. Technical Architecture & Data Strategy

### System Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                              UI LAYER                                  │
│                                                                        │
│  MainActivity  ──>  StreetSmartApp (Scaffold & BottomNavigation)       │
│                            │                                           │
│      ┌─────────────────────┼─────────────────────┬──────────────────┐  │
│      ▼                     ▼                     ▼                  ▼  │
│  DashboardScreen     IncomeExpenseForm     CashCheckScreen     StockScreen
│  & HistoryView       (Sales / Expenses)    (Reconciliation)    & Weather
└────────────────────────────────────────────────────────────────────────┘
                                     │
                                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│                           VIEWMODEL LAYER                              │
│                                                                        │
│                      StreetSmartViewModel                              │
│      - uiState: StateFlow<StreetSmartUiState>                          │
│      - dateFilter: StateFlow<LocalDate>                                │
│      - currentLanguage: StateFlow<AppLanguage> (EN / TA)               │
│      - addTransaction(), updateCashRegister(), updateStock(), etc.     │
└────────────────────────────────────────────────────────────────────────┘
                                     │
                                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│                          REPOSITORY LAYER                              │
│                                                                        │
│                        StreetSmartRepository                           │
│      - transactionsFlow, cashRegistersFlow, stockItemsFlow, savingsFlow│
│      - seedSampleDay(), resetDemoData(), clearAllData()                │
└────────────────────────────────────────────────────────────────────────┘
                                     │
                                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│                           ROOM DATABASE                                │
│                                                                        │
│   StreetSmartDatabase (Entities: Transaction, CashRegister, Stock, Goal)│
│   DAOs: TransactionDao, CashRegisterDao, StockDao, SavingsDao          │
└────────────────────────────────────────────────────────────────────────┘
```

### Room Schema & Entities
1. **`TransactionEntity`**:
   - `id: Long` (autoGenerate = true)
   - `type: String` ("INCOME" / "EXPENSE")
   - `amount: Double`
   - `paymentMethod: String` ("CASH" / "UPI")
   - `category: String` (e.g., "TEA_SNACKS", "PRODUCE", "STOCK_PURCHASE", "TRANSPORT", "PACKAGING", "RENT", "OTHER")
   - `notes: String`
   - `date: String` ("YYYY-MM-DD")
   - `timestamp: Long`
   - `isDemo: Boolean`
2. **`CashRegisterEntity`**:
   - `date: String` (PrimaryKey)
   - `openingCash: Double`
   - `cashSalesOverride: Double?`
   - `cashExpensesOverride: Double?`
   - `cashAdded: Double`
   - `cashWithdrawn: Double`
   - `actualClosingCash: Double`
   - `weatherTag: String` ("SUNNY", "RAINY", "CLOUDY", "FESTIVAL", "NORMAL")
   - `notes: String`
   - `isDemo: Boolean`
3. **`StockItemEntity`**:
   - `id: Long` (autoGenerate = true)
   - `itemName: String`
   - `unit: String` (kg, pieces, bundles, cups, plates)
   - `quantityPurchased: Double`
   - `quantitySold: Double`
   - `quantityRemaining: Double`
   - `purchaseCostPerUnit: Double`
   - `date: String`
   - `isDemo: Boolean`
4. **`SavingsGoalEntity`**:
   - `id: Int` (PrimaryKey = 1)
   - `targetAmount: Double`
   - `frequency: String` ("DAILY" / "WEEKLY")
   - `notes: String`

---

## 5. Verification & Testing Plan

1. **Compilation Check**: Verify Android build via `compile_applet`.
2. **Reconciliation Formula Verification**:
   - Verify: `Expected = 500 + 2000 + 0 - 700 - 0 = 1800`.
   - Verify: `Difference = 1750 - 1800 = -50`.
   - Verify: Warning displays highlighting the ₹50 shortage.
3. **Validation & State Integrity**:
   - Input fields reject negative numbers and non-numeric inputs.
   - Deleting a transaction updates the daily summary and cash reality check immediately.
   - Language toggle preserves user input and switches all static labels seamlessly between English and Tamil.
