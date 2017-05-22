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
package se.kth.app.sim;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.AppComp;
import se.kth.app.test.TestComp;
import se.kth.sim.compatibility.SimNodeIdExtractor;
import se.kth.system.HostMngrComp;
import se.sics.kompics.network.Address;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.adaptor.Operation;
import se.sics.kompics.simulator.adaptor.Operation1;
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution;
import se.sics.kompics.simulator.events.system.KillNodeEvent;
import se.sics.kompics.simulator.events.system.SetupEvent;
import se.sics.kompics.simulator.events.system.StartNodeEvent;
import se.sics.kompics.simulator.network.identifier.IdentifierExtractor;
import se.sics.ktoolbox.omngr.bootstrap.BootstrapServerComp;
import se.sics.ktoolbox.util.network.KAddress;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class ScenarioGen {
    private static final Logger LOG = LoggerFactory.getLogger(ScenarioGen.class);

    static Operation<SetupEvent> systemSetupOp = new Operation<SetupEvent>() {
        @Override
        public SetupEvent generate() {
            return new SetupEvent() {
                @Override
                public IdentifierExtractor getIdentifierExtractor() {
                    return new SimNodeIdExtractor();
                }
            };
        }
    };

    static Operation<StartNodeEvent> startBootstrapServerOp = new Operation<StartNodeEvent>() {

        @Override
        public StartNodeEvent generate() {
            return new StartNodeEvent() {
                KAddress selfAdr;

                {
                    selfAdr = ScenarioSetup.bootstrapServer;
                }

                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public Class getComponentDefinition() {
                    return BootstrapServerComp.class;
                }

                @Override
                public BootstrapServerComp.Init getComponentInit() {
                    return new BootstrapServerComp.Init(selfAdr);
                }
            };
        }
    };

    static Operation1<StartNodeEvent, Integer> startNodeOp = new Operation1<StartNodeEvent, Integer>() {

        @Override
        public StartNodeEvent generate(final Integer nodeId) {
            return new StartNodeEvent() {
                KAddress selfAdr;
                {

                    String nodeIp = "193.0.0." + nodeId;
                    selfAdr = ScenarioSetup.getNodeAdr(nodeIp, nodeId);
                    LOG.info("Node {} has been started", nodeId);

                }

                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public Class getComponentDefinition() {
                    return HostMngrComp.class;
                }

                @Override
                public HostMngrComp.Init getComponentInit() {
                    return new HostMngrComp.Init(selfAdr, ScenarioSetup.bootstrapServer, ScenarioSetup.croupierOId);
                }

                @Override
                public Map<String, Object> initConfigUpdate() {
                    Map<String, Object> nodeConfig = new HashMap<>();
                    nodeConfig.put("system.id", nodeId);
                    nodeConfig.put("system.seed", ScenarioSetup.getNodeSeed(nodeId));
                    nodeConfig.put("system.port", ScenarioSetup.appPort);
                    return nodeConfig;
                }
            };
        }
    };
/*TODO Set up nodes dying */
    public static final Operation1 killNodeOp = new Operation1<KillNodeEvent, Integer>() {
        @Override
        public KillNodeEvent generate(final Integer nodeId) {
            return new KillNodeEvent() {
                KAddress selfAdr;
                {

                    String nodeIp = "193.0.0." + nodeId;
                    selfAdr = ScenarioSetup.getNodeAdr(nodeIp, nodeId);
                    LOG.info("Node {} was killed", nodeId);

                }


                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public String toString() {
                    return "KillServer<" + selfAdr.toString() + ">";
                }
            };
        }
    };

    static Operation1<StartNodeEvent, Integer> startTestCompOp = new Operation1<StartNodeEvent, Integer>() {

        @Override
        public StartNodeEvent generate(final Integer nodeId) {
            return new StartNodeEvent() {
                KAddress selfAdr;

                {
                    selfAdr = ScenarioSetup.getNodeAdr("193.0.0", nodeId);


                }

                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public Class getComponentDefinition() {
                    return TestComp.class;
                }

                @Override
                public TestComp.Init getComponentInit() {
                    return new TestComp.Init(selfAdr, nodeId);
                }
            };
        }
    };



    public static SimulationScenario simpleSimulation() {
        SimulationScenario scen = new SimulationScenario() {
            {
                StochasticProcess systemSetup = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, systemSetupOp);
                    }
                };
                StochasticProcess startBootstrapServer = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, startBootstrapServerOp);
                    }
                };
                StochasticProcess startPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(5, startNodeOp, new BasicIntSequentialDistribution(1));
                    }
                };
                StochasticProcess startTestComp = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(100, startTestCompOp, new BasicIntSequentialDistribution(1));
                    }
                };

                StochasticProcess startKillNode = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(1, killNodeOp, new BasicIntSequentialDistribution(2));
                    }
                };

                systemSetup.start();
                startBootstrapServer.startAfterTerminationOf(1000, systemSetup);
                startPeers.startAfterTerminationOf(1000, startBootstrapServer);
                startTestComp.startAfterStartOf(100, startPeers);
                terminateAfterTerminationOf(10000, startTestComp);
            }
        };

        return scen;
    }

    public static SimulationScenario simpleKillNodeSimulation() {
        SimulationScenario scen = new SimulationScenario() {
            {
                StochasticProcess systemSetup = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, systemSetupOp);
                    }
                };
                StochasticProcess startBootstrapServer = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, startBootstrapServerOp);
                    }
                };
                StochasticProcess startPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(5, startNodeOp, new BasicIntSequentialDistribution(1));
                    }
                };
                StochasticProcess startTestComp = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(100, startTestCompOp, new BasicIntSequentialDistribution(1));
                    }
                };

                StochasticProcess startKillNode = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(1, killNodeOp, new BasicIntSequentialDistribution(2));
                    }
                };

                systemSetup.start();
                startBootstrapServer.startAfterTerminationOf(1000, systemSetup);
                startPeers.startAfterTerminationOf(1000, startBootstrapServer);
                startTestComp.startAfterStartOf(100, startPeers);
                startKillNode.startAfterStartOf(1000,startTestComp);

                terminateAfterTerminationOf(10000, startKillNode);
            }
        };

        return scen;
    }

    public static SimulationScenario simpleKillReviveNodeSimulation() {
        SimulationScenario scen = new SimulationScenario() {
            {
                StochasticProcess systemSetup = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, systemSetupOp);
                    }
                };
                StochasticProcess startBootstrapServer = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, startBootstrapServerOp);
                    }
                };
                StochasticProcess startPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(5, startNodeOp, new BasicIntSequentialDistribution(1));
                    }
                };
                StochasticProcess startTestComp = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(2, startTestCompOp, new BasicIntSequentialDistribution(0));
                    }
                };

                StochasticProcess startKillNode = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(1, killNodeOp, new BasicIntSequentialDistribution(1));
                    }
                };


                StochasticProcess startReviveNode = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(1, startNodeOp, new BasicIntSequentialDistribution(1));

                    }
                };

                systemSetup.start();
                startBootstrapServer.startAfterTerminationOf(1000, systemSetup);
                startPeers.startAfterTerminationOf(1000, startBootstrapServer);
                startTestComp.startAfterStartOf(100, startPeers);
                startKillNode.startAfterStartOf(5000,startTestComp);
                startReviveNode.startAfterStartOf(5000, startKillNode);
                terminateAfterTerminationOf(5000, startReviveNode);
            }
        };

        return scen;
    }

}
