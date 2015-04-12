import com.softwaremill.macwire._

object MacWirePlayground extends App {

  trait LogCreation {
    println(s"... assembling $this")
  }

  abstract class EnergySource extends LogCreation

  case class OilWell() extends EnergySource

  case class CoalMine() extends EnergySource

  case class EvilWorkers() extends LogCreation

  case class WastePlant(energySource: EnergySource, evilWorkers: EvilWorkers) extends LogCreation {
    def pollute(): Pollution = {
      val pollution = energySource match {
        case OilWell() => Pollution("Oil spills")
        case CoalMine() => Pollution("Smog")
      }
      println(s"${pollution.pollutionType} polluted Mother Earth.")
      pollution
    }
  }

  case class Pollution(pollutionType: String) extends LogCreation

  case class Earth() extends LogCreation

  case class Fire() extends LogCreation

  case class Wind() extends LogCreation

  case class Water() extends LogCreation

  case class Heart() extends LogCreation

  case class CaptainPlanet(earth: Earth, fire: Fire, wind: Wind, water: Water, hearth: Heart) {
    println(s"... assembling $this. By your powers combined.. I am Captain Planet!")

    def eliminate(pollution: Pollution) = {
      println(s"${pollution.pollutionType} eliminated. Go Planet!")
    }
  }

  trait EnergySourceOilModule {
    lazy val energySource = wire[OilWell]
  }

  trait EnergySourceCoalModule {
    lazy val energySource = wire[CoalMine]
  }

  trait PollutionModule {
    def wastePlant = wire[WastePlant]

    lazy val evilWorkers = wire[EvilWorkers]

    def energySource: EnergySource
  }

  trait PlaneteersModule {
    lazy val captainPlanet = wire[CaptainPlanet]

    lazy val earth = wire[Earth]
    lazy val fire = wire[Fire]
    lazy val wind = wire[Wind]
    lazy val water = wire[Water]
    lazy val heart = wire[Heart]
  }

  trait AppModule extends PlaneteersModule with PollutionModule {

    def go = {
      println(1 to 30 map (_ => "=") mkString (""))
      val pollution = wastePlant.pollute()
      captainPlanet.eliminate(pollution)
    }
  }

  val oilModule = new AppModule with EnergySourceOilModule

  val coalModule = new AppModule with EnergySourceCoalModule

  oilModule.go
  oilModule.go
  oilModule.go

  coalModule.go

}
