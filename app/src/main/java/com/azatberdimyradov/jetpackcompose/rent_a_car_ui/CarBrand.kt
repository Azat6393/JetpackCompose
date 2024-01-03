package com.azatberdimyradov.jetpackcompose.rent_a_car_ui

import com.azatberdimyradov.jetpackcompose.R

data class CarBrand(
    val icon: Int,
    val name: String,
    val isSelected: Boolean = false
) {
    companion object {
        fun getCars(): List<CarBrand> {
            return listOf(
                CarBrand(
                    icon = R.drawable.bmw,
                    name = "BMW"
                ),
                CarBrand(
                    icon = R.drawable.ferrari,
                    name = "Ferrari"
                ),
                CarBrand(
                    icon = R.drawable.tesla,
                    name = "Tesla"
                ),
                CarBrand(
                    icon = R.drawable.toyota,
                    name = "Toyota"
                ),
                CarBrand(
                    icon = R.drawable.volkswagen,
                    name = "Volkswagen"
                ),
            )
        }
    }
}
