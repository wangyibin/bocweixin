package controllers

import play.api._
import play.api.mvc._
import java.security.MessageDigest

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  def checkSignature(signature: String, timestamp: String, nonce: String, echoStr: String) = Action {
    val message = List("boc", timestamp, nonce).sorted.mkString
    val sha1Result = sha1(message)
    Logger.info(s"""signature: $signature, timestamp: $timestamp, nonce: $nonce, echoStr: $echoStr
sha1Result:$sha1Result""")
    if (sha1Result == signature) {
      Logger.info("verify success")
      Ok(echoStr).as("plain/text")
    } else {
      Ok("").as("plain/text")
    }
  }
  private def sha1(message: String) = {
    val messageDigest = MessageDigest.getInstance("SHA1")
    messageDigest.update(message.getBytes())
    messageDigest.digest().map{byte => 
      if ((0xff & byte) < 0x10) {"0"} else {""} + Integer.toHexString((0xFF & byte))
    }.mkString
    
  }
}
