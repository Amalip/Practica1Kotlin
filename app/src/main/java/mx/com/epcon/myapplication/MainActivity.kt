package mx.com.epcon.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var txvInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txvInfo = findViewById(R.id.txvInfo)
    }

    private val pets = mutableListOf<Animal>()
    private val dogs =
        arrayOf(
            Dog("Doggy", 15, "Chihuahua"),
            Dog("Luck", 10, "Maltese"),
            Dog("Cheems", 17)
        )
    private val cats =
        arrayOf(
            Cat("Minino", 10),
            Cat("Felix", 13, "Egyptian"),
            Cat("Lucifer", 12)
        )

    private var counter = -1
    private var catCounter = 0
    private var dogCounter = 0

    private lateinit var actualPet: Animal

    fun createNewPet(view: View) {
        val evenOrOdd = Random.nextInt() % 2 == 0
        var newPet: Animal? = null

        when {
            evenOrOdd && dogCounter < 3 -> newPet = dogs[dogCounter]
            !evenOrOdd && catCounter < 3 -> newPet = cats[catCounter]
            else -> makeToast("We don't have more ${if (evenOrOdd) "dogs" else "cats"}, try again.")
        }

        newPet?.let {
            when (newPet) {
                is Dog -> dogCounter++
                is Cat -> catCounter++
            }

            setActualPet(it)
            pets.add(it)
            counter = pets.size - 1
        }
    }

    fun getPreviousPet(view: View) {
        if (counter - 1 != -1 && counter - 1 >= 0) {
            setActualPet(pets[counter - 1])
            counter--
        }
    }

    fun getNextPet(view: View) {
        if (counter != -1 && counter + 1 <= pets.size - 1) {
            setActualPet(pets[counter + 1])
            counter++
        }
    }

    private fun setActualPet(animal: Animal) {
        actualPet = animal
        setBasicInfo()
    }

    private fun setBasicInfo() {
        txvInfo.text = actualPet.getBasicInfo()
    }

    private fun makeToast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

    fun makeSound(view: View) = ::actualPet.isInitialized.let {
        if (it) makeToast(actualPet.makeSound())
    }

    fun play(view: View) = ::actualPet.isInitialized.let {
        if (it) makeToast(actualPet.play())
    }

    fun eat(view: View) = ::actualPet.isInitialized.let {
        if (it) makeToast(actualPet.eat())
    }

}

abstract class Animal(
    protected val name: String = "",
    private val age: Int = 0,
    protected val race: String? = null
) {

    abstract val type: String

    fun getBasicInfo() =
        "Your animal is a: $type \n its name is: $name \n its age is $age \n and its race is: ${race ?: "NA"}"

    abstract fun makeSound(): String
    abstract fun play(): String
    abstract fun eat(): String

}

class Dog(name: String, age: Int, race: String? = null) : Animal(name, age, race) {

    override val type = "Dog"

    override fun makeSound() = "WOOF WOOF"

    override fun play() = "$name is playing with its ball"

    override fun eat() = race?.let {
        "$name is eating dog food for its race $it"
    } ?: "No enough information"

}

class Cat(name: String, age: Int, race: String? = null) : Animal(name, age, race) {

    override val type = "Cat"

    override fun makeSound() = "MIAU MIAU"

    override fun play() = "$name is playing with its hairball"

    override fun eat() = race?.let {
        "$name is eating fish for its race $it"
    } ?: "No enough information"

}