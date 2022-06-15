package victor.testing.prework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 *  Write automated tests for the code below, such that any line here is altered (changed in any way) a test would fail indicating a bug.
 *  Assume the code is super-critical. Human lives depend on it.
 *  Therefore, if any refactoring is required before testing, only perform 100% safe moves.
 *  For some methods, you might need to rely on mocks or alternative dummy implems.
 */
public class NuclearService {
   private static final Logger log = LoggerFactory.getLogger(NuclearService.class);

   int phaserThreshold;
   private final DistanceService distanceService;
   private final ProbeService probeService;

   public NuclearService(DistanceService distanceService, ProbeService probeService) {
      this.distanceService = distanceService;
      this.probeService = probeService;
   }

   // TODO test me
   public double interpolateQuantum(int atoms, double mark1) {
      if (atoms - phaserThreshold < 5) {
         throw new IllegalStateException("Too few atoms");
      }
      log.debug("Processing mass " + atoms);
      double mark2 = 0;
      if (atoms > 2 * phaserThreshold) {
         mark2 = mark1 / 2;
      }
      return atoms * mark1 + mark1 * mark1 - mark2;
   }


   // TODO test me
   public boolean hasRequiredMass(int quanta, int speed, int destinationSolarSystemId) {
      return distanceService.distanceTo(destinationSolarSystemId) / speed > quanta;
   }

   // TODO test me
   public void reportProbeStats(Coords coord, double gravity) {
      if (!probeService.hasStatsNear(coord)) {
         probeService.reportStats(coord, gravity, LocalDateTime.now());
      } else {
         log.debug("Ignoring duplicate stats at: " + coord);
      }
   }
}


class Coords {
}

class DistanceService {
   public long distanceTo(int destinationSolarSystemId) {
      throw new IllegalStateException("Can't call this from tests!");
   }
}

class ProbeService {
   public boolean hasStatsNear(Coords coord) {
      throw new IllegalStateException("Can't call this from tests!");
   }

   public void reportStats(Coords coord, double gravity, LocalDateTime timeStamp) {
      throw new IllegalStateException("Can't call this from tests!");
   }
}