package com.jasjeet.marketit.model

import com.jasjeet.marketit.ui.listings.ListingsFragment
import com.jasjeet.marketit.util.Resource

/** Ui state class for [ListingsFragment].
 * @param listings Our product listings of type [ListingData].
 * @param error Any error incurred should be depicted by this of type [ResponseError].
 * @param status Status of the resulting API call for [listings] of type [Resource.Status].*/
data class ListingsUiState(
    val listings: ListingData? = null,
    val error: ResponseError? = null,
    val status: Resource.Status = Resource.Status.LOADING
)