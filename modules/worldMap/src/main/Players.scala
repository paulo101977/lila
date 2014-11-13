package lila.worldMap

private[worldMap] object Players {

  // to each game ID, associate list of player Locations
  // there can be 0, 1 or 2 players per game ID,
  // but this constraint is not expressed by the cache type :(
  private val cache = org.mapdb.DBMaker.newCacheDirect[String, List[Location]](0.2)

  def getOpponentLocation(gameId: String, myLocation: Location): Option[Location] =

    Option(cache get gameId) getOrElse Nil match {

      // new game ID, store player location
      case Nil =>
        cache.put(gameId, List(myLocation)); None

      // only my location is known
      case List(loc) if loc == myLocation => None

      // only opponent location is known. Store mine
      case List(loc) =>
        cache.put(gameId, List(loc, myLocation)); Some(loc)

      // both locations are known
      case List(l1, l2) if l1 == myLocation => Some(l2)

      // both locations are known
      case List(l1, l2) if l2 == myLocation => Some(l1)
    }
}

