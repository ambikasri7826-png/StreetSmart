package com.example.localization

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    TAMIL("ta", "தமிழ்")
}

object Strings {
    fun get(key: String, language: AppLanguage): String {
        val entry = localizedMap[key] ?: return key
        return if (language == AppLanguage.TAMIL) entry.tamil else entry.english
    }

    private data class StringEntry(val english: String, val tamil: String)

    private val localizedMap = mapOf(
        "app_title" to StringEntry("StreetSmart", "ஸ்ட்ரீட் ஸ்மார்ட்"),
        "tagline" to StringEntry("Don’t just track what you earn. Understand what you keep.", "வருமானத்தை மட்டும் பார்க்காதீர்கள். கையில் மிஞ்சுவதைப் புரிந்து கொள்ளுங்கள்."),
        
        // Navigation Tabs
        "nav_dashboard" to StringEntry("Dashboard", "முகப்பு"),
        "nav_income" to StringEntry("Sales", "விற்பனை"),
        "nav_expenses" to StringEntry("Expenses", "செலவுகள்"),
        "nav_cash_check" to StringEntry("Cash Check", "பண சரிபார்ப்பு"),
        "nav_stock" to StringEntry("Stock", "சரக்கு"),
        "nav_reports" to StringEntry("Reports", "அறிக்கைகள்"),
        "nav_savings" to StringEntry("Savings", "சேமிப்பு"),
        "nav_history" to StringEntry("History", "வரலாறு"),

        // Dashboard Metrics
        "total_sales" to StringEntry("Total Sales", "மொத்த விற்பனை"),
        "cash_sales" to StringEntry("Cash Sales", "ரொக்க விற்பனை"),
        "upi_sales" to StringEntry("UPI Sales", "UPI டிஜிட்டல்"),
        "total_expenses" to StringEntry("Total Expenses", "மொத்த செலவுகள்"),
        "operating_profit" to StringEntry("Est. Operating Profit", "மதிப்பிடப்பட்ட லாபம்"),
        "profit_disclaimer" to StringEntry("Estimated: True profit depends on unsold stock and unrecorded costs.", "மதிப்பீடு: விற்பனையாகாத சரக்கு மற்றும் மறைமுக செலவுகள் உண்மையான லாபத்தை மாற்றலாம்."),
        "reality_check_status" to StringEntry("Cash Reality Check", "பண நிலைமை சரிபார்ப்பு"),
        "unsold_stock_count" to StringEntry("Unsold Stock", "விற்பனையாகாத சரக்கு"),

        // Cash Reality Check
        "opening_cash" to StringEntry("Opening Cash in Box", "தொடக்க கையிருப்பு ரொக்கம்"),
        "cash_added" to StringEntry("Cash Added (Deposit/Loan)", "கூடுதல் ரொக்கம் சேர்த்தது"),
        "cash_expenses_paid" to StringEntry("Cash Expenses Paid", "ரொக்கமாக செலுத்திய செலவு"),
        "cash_withdrawn" to StringEntry("Cash Withdrawn (Personal)", "எடுத்த ரொக்கம் (சொந்த செலவு)"),
        "expected_closing_cash" to StringEntry("Expected Closing Cash", "இருக்க வேண்டிய ரொக்கம்"),
        "actual_closing_cash" to StringEntry("Actual Closing Cash Counted", "கையில் இருக்கும் ரொக்கம்"),
        "cash_difference" to StringEntry("Cash Difference", "பண வித்தியாசம்"),
        "formula_title" to StringEntry("Reconciliation Formula", "கணக்கீட்டு சூத்திரம்"),
        "formula_text" to StringEntry("Expected = Opening (₹) + Cash Sales (₹) + Added (₹) − Expenses (₹) − Withdrawn (₹)", "இருக்க வேண்டியது = தொடக்கம் (₹) + ரொக்க விற்பனை (₹) + சேர்த்தது (₹) − செலவு (₹) − எடுத்தது (₹)"),

        // Discrepancy Warnings
        "shortage_warning" to StringEntry(
            "⚠️ Shortage detected! Cash is unaccounted for. Review today's transactions, check for forgotten tea/snack expenses, or change calculation errors.",
            "⚠️ ரொக்க பற்றாக்குறை! பணம் குறைவாக உள்ளது. இன்றைய செலவுகள், சில்லறை தவறு அல்லது பதிவு செய்யாத செலவுகளை சரிபார்க்கவும்."
        ),
        "surplus_note" to StringEntry(
            "ℹ️ Surplus detected! Extra cash in drawer. Check for unrecorded cash sales or customer tip payments.",
            "ℹ️ கூடுதல் ரொக்கம்! பெட்டியில் அதிக பணம் உள்ளது. பதிவு செய்ய மறந்த ரொக்க விற்பனை உள்ளதா என பார்க்கவும்."
        ),
        "balanced_note" to StringEntry(
            "✅ Perfectly balanced! Every rupee matches your records.",
            "✅ அருமை! உங்கள் கணக்கும் பெட்டியில் உள்ள பணமும் சரியாக பொருந்துகிறது."
        ),

        // Actions
        "add_sale" to StringEntry("Add Sale", "விற்பனை சேர்"),
        "add_expense" to StringEntry("Add Expense", "செலவு சேர்"),
        "save_changes" to StringEntry("Save Changes", "சேமி"),
        "delete" to StringEntry("Delete", "நீக்கு"),
        "edit" to StringEntry("Edit", "திருத்து"),
        "reset_demo" to StringEntry("Reset Sample Day", "மாதிரி நாளை மீட்டமை"),
        "clear_all" to StringEntry("Clear All Data", "அனைத்தையும் அழி"),
        "demo_badge" to StringEntry("SAMPLE DEMO DAY", "மாதிரி வணிக நாள்"),
        "real_data_badge" to StringEntry("LIVE RECORD", "உண்மைப் பதிவு"),
        "filter_today" to StringEntry("Today", "இன்று"),
        "filter_yesterday" to StringEntry("Yesterday", "நேற்று"),
        "filter_all" to StringEntry("All Dates", "அனைத்து நாட்கள்"),

        // Stock Tracker
        "item_name" to StringEntry("Item Name", "பொருள் பெயர்"),
        "purchased_qty" to StringEntry("Quantity Purchased", "வாங்கிய அளவு"),
        "sold_qty" to StringEntry("Quantity Sold", "விற்ற அளவு"),
        "remaining_qty" to StringEntry("Quantity Remaining", "மீதமுள்ள அளவு"),
        "unsold_qty" to StringEntry("Unsold / Spoiled Qty", "விற்பனையாகாத / சேதமான அளவு"),
        "wastage_warning" to StringEntry("⚠️ Stock wastage recorded! Review supplier purchase quantities.", "⚠️ சரக்கு வீணாகியுள்ளது! அடுத்த முறை வாங்கும் அளவை சரிபார்க்கவும்."),
        "unit_kg" to StringEntry("Kg", "கிலோ"),
        "unit_pieces" to StringEntry("Pieces", "எண்ணிக்கை"),
        "unit_bunches" to StringEntry("Bunches", "கட்டுகள்"),
        "unit_cups" to StringEntry("Cups", "கப்புகள்"),
        "unit_plates" to StringEntry("Plates", "தட்டுகள்"),

        // Weather & Day Types
        "weather_tag" to StringEntry("Weather / Day Type", "வானிலை / நாள் வகை"),
        "tag_sunny" to StringEntry("Sunny ☀️", "வெயில் ☀️"),
        "tag_rainy" to StringEntry("Rainy 🌧️", "மழை 🌧️"),
        "tag_cloudy" to StringEntry("Cloudy ⛅", "மேகமூட்டம் ⛅"),
        "tag_festival" to StringEntry("Festival / Market 🎪", "திருவிழா / சந்தை 🎪"),
        "tag_normal" to StringEntry("Normal Day 🛒", "சாதாரண நாள் 🛒"),

        // Savings Goal
        "savings_goal" to StringEntry("Savings Goal", "சேமிப்பு இலக்கு"),
        "daily_target" to StringEntry("Daily Target", "தினசரி இலக்கு"),
        "weekly_target" to StringEntry("Weekly Target", "வாராந்திர இலக்கு"),
        "savings_progress" to StringEntry("Savings Progress", "சேமிப்பு முன்னேற்றம்"),
        "savings_caution" to StringEntry("Caution: Never treat all leftover cash as profit or savings. Preserve reserve for next morning's wholesale stock.", "எச்சரிக்கை: மீதமுள்ள பணத்தை முழுமையாக லாபமாக எண்ணாதீர்கள். நாளை காலை மொத்த சரக்கு வாங்க பணம் தேவை."),

        // Categories - Sales
        "cat_tea_coffee" to StringEntry("Tea & Coffee", "டீ & காபி"),
        "cat_snacks" to StringEntry("Chai Snacks / Vada", "வடை / சிற்றுண்டி"),
        "cat_meals" to StringEntry("Tiffin / Meals", "டிபன் / சாப்பாடு"),
        "cat_vegetables" to StringEntry("Vegetables", "காய்கறிகள்"),
        "cat_fruits" to StringEntry("Fruits", "பழங்கள்"),
        "cat_flowers" to StringEntry("Flowers", "பூக்கள்"),
        "cat_general_sale" to StringEntry("General Sale", "பொது விற்பனை"),

        // Categories - Expenses
        "cat_stock_purchase" to StringEntry("Wholesale Stock", "மொத்த கொள்முதல்"),
        "cat_transport" to StringEntry("Transport / Auto", "போக்குவரத்து / ஆட்டோ"),
        "cat_packaging" to StringEntry("Packaging & Bags", "பைகள் / கவர்கள்"),
        "cat_rent_bribe" to StringEntry("Cart / Stall Rent", "இட வாடகை / கட்டணம்"),
        "cat_electricity_ice" to StringEntry("Ice / Power / Battery", "ஐஸ் / மின்சாரம்"),
        "cat_gas_fuel" to StringEntry("Gas Cylinder / Fuel", "எரிவாயு / எரிபொருள்"),
        "cat_oil_groceries" to StringEntry("Cooking Oil & Masala", "எண்ணெய் & மசாலா"),
        "cat_other_expense" to StringEntry("Other Expense", "இதர செலவு"),

        // Payment Methods
        "pay_cash" to StringEntry("Cash", "ரொக்கம்"),
        "pay_upi" to StringEntry("UPI / QR", "UPI / QR"),
        "notes_hint" to StringEntry("Optional notes (e.g., Morning mandi, tomato batch)", "குறிப்பு (எ.கா: காலை மார்க்கெட், தக்காளி)"),
        "amount_hint" to StringEntry("Amount in ₹", "தொகை ₹-ல்"),
        "no_data" to StringEntry("No records found for this date. Tap + to add.", "இந்த தேதியில் பதிவுகள் இல்லை. சேர்க்க + அழுத்தவும்."),

        // Vendor Tips
        "vendor_tips_header" to StringEntry("Vendor Smart Tips", "விற்பனையாளர் வழிகாட்டி"),
        "tip_next" to StringEntry("Next Tip ↻", "அடுத்த குறிப்பு ↻"),

        // Savings Plan
        "savings_plan_header" to StringEntry("Targeted Savings Plan", "இலக்குடன் கூடிய சேமிப்புத் திட்டம்"),
        "plan_emergency" to StringEntry("Emergency / Medical Fund", "அவசர & மருத்துவ நிதி"),
        "plan_repair" to StringEntry("Cart & Equipment Repair", "வண்டி & உபகரணங்கள் பழுதுபார்ப்பு"),
        "plan_wholesale" to StringEntry("Wholesale Mandi Reserve", "மொத்த கொள்முதல் இருப்பு நிதி"),
        "plan_loan" to StringEntry("Chit Fund / Loan Repayment", "சீட்டு & கடன் தவணை செலுத்துதல்"),
        "plan_festival" to StringEntry("Festival Season Buffer", "பண்டிகைக் கால சேமிப்பு"),
        "deposit_recommended" to StringEntry("Safe Daily Deposit", "பரிந்துரைக்கப்பட்ட தொகை"),
        "add_deposit_btn" to StringEntry("Log Deposit", "சேமிப்பில் சேர்"),
        "accumulated_so_far" to StringEntry("Accumulated Savings", "சேமிக்கப்பட்ட மொத்த தொகை"),
        "target_goal" to StringEntry("Target Goal", "இலக்குத் தொகை")
    )
}
