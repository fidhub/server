package model

import java.util.UUID

import conf.util.Util
import domain.User
import play.api.libs.json.Json

/**
 * Created by hashcode on 2015/04/16.
 */
case class UserModel(otherName: String,
                     firstName: String,
                     lastName: String,
                     username: String,
                     enable: Boolean,
                     password: String,
                     role: List[String],
                     contact: List[String],
                     address: List[String]
                      ) {
  def getDomain(): User = UserModel.domain(this)
}

object UserModel {
  implicit val userFmt = Json.format[UserModel]

  def domain(model: UserModel) = {
    User(
      Util.md5Hash(UUID.randomUUID().toString()),
      model.otherName,
      model.firstName,
      model.lastName,
      model.username,
      model.enable,
      model.password,
      model.role,
      model.contact,
      model.address
    )
  }
}