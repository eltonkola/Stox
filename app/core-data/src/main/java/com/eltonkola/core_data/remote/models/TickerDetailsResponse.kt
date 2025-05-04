package com.eltonkola.core_data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Represents the top-level response structure for the Ticker Details API call.
 */
data class StockDetailsResponse(
    @SerializedName("request_id")
    val requestId: String,

    @SerializedName("results")
    val results: StockDetails, // The main ticker information

    @SerializedName("status")
    val status: String
)

/**
 * Contains the detailed information about a specific ticker.
 */

data class StockDetails(
    @SerializedName("ticker")
    val ticker: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("market")
    val market: String,

    @SerializedName("locale")
    val locale: String,

    @SerializedName("primary_exchange")
    val primaryExchange: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("active")
    val active: Boolean,

    @SerializedName("currency_name")
    val currencyName: String,

    @SerializedName("cik")
    val cik: String? = null, // CIK might not always be present

    @SerializedName("composite_figi")
    val compositeFigi: String? = null,

    @SerializedName("share_class_figi")
    val shareClassFigi: String? = null,

    @SerializedName("market_cap")
    val marketCap: Double? = null, // Use Double for large numbers or potential decimals

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("address")
    val address: StockAddress? = null, // Nested address object

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("sic_code")
    val sicCode: String? = null,

    @SerializedName("sic_description")
    val sicDescription: String? = null,

    @SerializedName("ticker_root")
    val tickerRoot: String? = null,

    @SerializedName("homepage_url")
    val homepageUrl: String? = null,

    @SerializedName("total_employees")
    val totalEmployees: Int? = null,

    @SerializedName("list_date")
    val listDate: String? = null, // Keep as String, parse to Date/LocalDate if needed

    @SerializedName("branding")
    val branding: StockBranding? = null, // Nested branding object

    @SerializedName("share_class_shares_outstanding")
    val shareClassSharesOutstanding: Long? = null, // Use Long for large integer counts

    @SerializedName("weighted_shares_outstanding")
    val weightedSharesOutstanding: Long? = null, // Use Long

    @SerializedName("round_lot")
    val roundLot: Int? = null
)

/**
 * Represents the address information for a company.
 */
data class StockAddress(
    @SerializedName("address1")
    val address1: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("postal_code")
    val postalCode: String? = null
){
    val address: String by lazy {
        formatAddress()
    }

    private fun formatAddress(): String {
        val addr = address1?.trim()?.takeIf { it.isNotBlank() }
        val cty = city?.trim()?.takeIf { it.isNotBlank() }
        val st = state?.trim()?.takeIf { it.isNotBlank() }
        val zip = postalCode?.trim()?.takeIf { it.isNotBlank() }

        val cityStateZip = buildList {
            cty?.let { add(it) }
            st?.let { add(it) }
        }.joinToString(", ")
            .let { cityState ->
                listOfNotNull(cityState.takeIf { it.isNotEmpty() }, zip).joinToString(" ")
            }

        return listOfNotNull(addr, cityStateZip.takeIf { it.isNotEmpty() }).joinToString(" ")
    }
}

/**
 * Represents the branding information (logos) for a company.
 */
data class StockBranding(
    @SerializedName("logo_url")
    val logoUrl: String? = null,

    @SerializedName("icon_url")
    val iconUrl: String? = null
)
