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

    fun findSubdirsSmallerThan(size: Int): List<Entity> {
        val final = mutableListOf<Entity>()
        for (dir in this.children) {
            if (dir is Dir) {
                if (dir.size <= size) {
                    final.add(dir)
                }
                final.addAll(dir.findSubdirsSmallerThan(size))
            }
        }

        return final
    }

    fun findSubdirsBiggerThan(size: Int): List<Entity> {
        val final = mutableListOf<Entity>()
        for (dir in this.children) {
            if (dir is Dir) {
                if (dir.size >= size) {
                    final.add(dir)
                }
                final.addAll(dir.findSubdirsBiggerThan(size))
            }
        }

        return final
    }

    fun appendChildren(entity: Entity) {
        this.children += entity
    }

    fun findChild(name: String): Entity? {
        return this.children.find { it.name == name }
    }
}
