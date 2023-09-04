package days.day7.entities

abstract class Entity(
    val name: String,
    open val size: Int,
)


class File(name: String, size: Int) : Entity(name, size)

class Dir(name: String) : Entity(name, 0) {
    private val children: MutableList<Entity> = mutableListOf()

    override val size: Int
        get() = this.children.sumOf { it.size }

    fun appendChildren(entity: Entity) {
        this.children += entity
    }
}
