package com.example.efisherytaskapplication.data

enum class SortType(val desc: String) {
    DEFAULT("Default"),
    A_TO_Z("A ke Z"),
    Z_TO_A("Z ke A"),
    PRICE_ASC("Harga Termurah"),
    PRICE_DESC("Harga Termahal"),
    SIZE_ASC("Ukuran Terkecil"),
    SIZE_DESC("Ukuran Terbesar")
}