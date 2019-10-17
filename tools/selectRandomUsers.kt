/**
 * モブプログラミングで、担当する人をランダムで選択する
 *
 * @author ikki
 * @see https://paiza.io/projects/SHMoTiDcBPG9eI86P-WS5A
 */
fun main(args: Array<String>) {
    // Your code here!
    
    val member = mutableListOf("kazuyuki", "yu", "adachi", "be-", "ikki")
    val memberR = mutableListOf(member.shuffled())
    
    println("今日の順番は${memberR[0]}です")
    println("３０分交代です。頑張ってください")
}
