package java.com.arcanavault.model.data

interface ICatalogItem {
    val name: String
    val attributes: List<Attribute>
    var isFavorite: Boolean
}
