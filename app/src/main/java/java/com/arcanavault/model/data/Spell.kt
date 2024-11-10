package java.com.arcanavault.model.data

class Spell (
    override val name: String,
    override val attributes: List<Attribute>,
    override var isFavorite: Boolean
) : ICatalogItem

