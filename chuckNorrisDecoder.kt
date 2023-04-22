import kotlin.system.exitProcess

fun main() {
    mainMenu()
}

fun mainMenu() {
    println("\nPlease input operation (encode/decode/exit):")
    val input = readln()

    while (true) {
        if (input == "encode") {
            println("Input string: ")
            val inputString = readln()

            println("Encoded string: ")
            encode(inputString)
        } else if (input == "exit") {
            println("Bye!"); exitProcess(1)

        } else if (input == "decode") {
            println("Input encoded string:")
            val inputString = readln()
            val zeroGroups = inputString.trim().split(" ").chunked(2)

            // Check if input consists of 0 and spaces only
            for (char in inputString) {
                if (char != '0' && char != ' ') {
                    println("Input not valid: input should only consist of 0 and spaces")
                    mainMenu()
                }
            }
            // Check if first block of each sequence is not 0 or 00
            for (block in zeroGroups) {
                if (block[0] != "0" && block[0] != "00") {
                    println("Input not valid: each block should start with 0 or 00")
                    mainMenu()
                }
            }
            // Check if number of blocks is odd
            val zeroGroups1 = inputString.trim().split(" ").chunked(1)
            if (zeroGroups1.size % 2 == 1) {
                println("Input not valid: number of blocks is odd")
                mainMenu()
            }
            // If all valid, call decode function
            decode(inputString)

        } else {
            println("There is no '$input' operation")
            mainMenu()
        }
    }

}

fun decode(input: String): String {

    // Grouping input string into blocks of [0, xx] or [00, xx]
    val zeroGroups = input.trim().split(" ").chunked(2)

    var binaryStringList = mutableListOf<String>()

    // For every block we add 1 or 0 depending on whether it's 0 or 00 respectively
    for (element in zeroGroups) {
        if (element[0] == "0") {
            repeat(element[1].length) {
                binaryStringList.add("1")
            }
        } else {
            repeat(element[1].length) {
                binaryStringList.add("0")
            }
        }
    }
    // Convert it all to a string
    val binaryString = binaryStringList.joinToString("")

    // Checking if the binary string length is a multiple of 7
    if (binaryString.length % 7 != 0) {
        println("Input not valid: decoded string is not multiple of 7")
        mainMenu()
    }

    // Print the decoded string
    val decoded = binaryToString(binaryString)
    println("Decoded string:")
    println(decoded)
    mainMenu()

    return "0"
}

fun encode(input: String): String {

    //initializing empty mutable list of type String
    var binaryList = mutableListOf<String>()

    //converting each element (character) of input string to binary and adding padding of 0's to the beginning
    //padStart = 7 because we need a 7-bit binary number
    //add binary numbers to the binaryList we created earlier
    for (element in input) {
        binaryList.add(Integer.toBinaryString(element.code).padStart(7, '0'))
    }

    //not most elegant, but I created another variable where we join the elements of list to a single string
    val bin = binaryList.joinToString("")
    //then creating an array that will store the character array of 0's and 1's
    //example 1000011 -> [1, 0000, 11]
    val arr = splitDistinct(bin.toCharArray())

    //loop to iterate through indices of the array (example [1, 0000, 11])
    for (i in arr.indices) {
        //if first element of i element of array (in my example it's '1'), we print a new block starting with "0 "
        if (arr[i][0] == '1') {
            print("0 ")
            //then print more 0's based on the length of element of array
            repeat (arr[i].length) {
                print("0")
            }
            print(" ") //printing a space
            //similarly, if first element of i element of array [0000] we print a new block starting with "00 "
        } else if (arr[i][0] == '0') {
            print("00 ")
            repeat (arr[i].length) {
                print("0")
            }
            print(" ")
        }
    }
    mainMenu()
    return "0"
}

fun binaryToString(binary: String): String {
    val chunks = binary.chunked(7) // split binary into groups of 7 bits
    return chunks.map { it.toInt(2).toChar() }.joinToString("") // convert each group to a character and join them
}

fun splitDistinct(arr: CharArray): List<String> = arr
        .fold(ArrayList<StringBuilder>()) { acc, ch -> acc.apply {
            if (isEmpty() || last().last() != ch)
                add(StringBuilder(ch.toString()))
            else
                last().append(ch)
        }}
        .map { it.toString() }
