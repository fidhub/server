package repository

import com.datastax.driver.core.{ResultSet, Row}
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.Implicits._
import com.websudos.phantom.column.DateColumn
import com.websudos.phantom.iteratee.Iteratee
import com.websudos.phantom.keys.PartitionKey
import conf.connection.DataConnection
import domain.RawContent

import scala.concurrent.Future

/**
  * Created by hashcode on 2016/02/11.
  */
class RawContentRepository extends CassandraTable[RawContentRepository, RawContent] {


  object id extends StringColumn(this) with PartitionKey[String]

  object dateCreated extends DateColumn(this)

  object creator extends StringColumn(this)

  object source extends StringColumn(this)

  object category extends StringColumn(this)

  object title extends StringColumn(this)

  object content extends StringColumn(this)

  object contentType extends StringColumn(this)

  object status extends StringColumn(this)

  object state extends StringColumn(this)

  override def fromRow(row: Row): RawContent = {
    RawContent(
      id(row),
      dateCreated(row),
      creator(row),
      source(row),
      category(row),
      title(row),
      content(row),
      contentType(row),
      status(row),
      state(row)
    )
  }
}

object RawContentRepository extends RawContentRepository with DataConnection {
  override lazy val tableName = "rcontent"

  def save(content: RawContent): Future[ResultSet] = {
    insert
      .value(_.id, content.id)
      .value(_.dateCreated, content.dateCreated)
      .value(_.creator, content.creator)
      .value(_.source, content.source)
      .value(_.category, content.category)
      .value(_.title, content.title)
      .value(_.content, content.content)
      .value(_.contentType, content.contentType)
      .value(_.status, content.status)
      .value(_.state, content.state)
      .future()
  }

  def getContentById(id: String): Future[Option[RawContent]] = {
    select.where(_.id eqs id).one()
  }

  def getAllContents: Future[Seq[RawContent]] = {
    select.fetchEnumerator() run Iteratee.collect()
  }

  def getContents(startValue: Int): Future[Iterator[RawContent]] = {
    select.fetchEnumerator() run Iteratee.slice(startValue, 20)
  }
}
