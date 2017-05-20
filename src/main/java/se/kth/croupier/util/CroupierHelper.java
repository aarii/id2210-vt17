/*
 * 2016 Royal Institute of Technology (KTH)
 *
 * LSelector is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.kth.croupier.util;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sics.ktoolbox.croupier.event.CroupierSample;
import se.sics.ktoolbox.omngr.bootstrap.BootstrapClientComp;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.other.AgingAdrContainer;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class CroupierHelper {
  private static final Logger LOG = LoggerFactory.getLogger(BootstrapClientComp.class);

  public static List<KAddress> getSample(CroupierSample<Boolean> sample) {
    List<KAddress> s = new LinkedList<>();
   LOG.debug("VI ÄR I CROUPIERHELPER");
    for(AgingAdrContainer<KAddress, Boolean> e: sample.publicSample.values()) {
      s.add(e.getSource());
    }
    return s;
  }
}
