package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Game;

import java.util.List;

public final class StudentController implements DefenderController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int kaylasGhost(Game game) {

		if (game.getDefender(3).isVulnerable()) {
			return game.getDefender(3).getNextDir(game.getAttacker().getLocation(), false);
		} else {
			return game.getDefender(3).getNextDir(game.getAttacker().getLocation(), true);
		}
	}

	public int nicolesGhost(Game game) {

		//Attacker atk = game.getAttacker();

		//if pacman close to power pill, do NOT approach

		int direction = game.getAttacker().getDirection();

		if (game.getDefender(2).isVulnerable()) {
			return game.getDefender(2).getNextDir(game.getAttacker().getLocation(), false);
		} else {
			if (game.getAttacker().getLocation().getNeighbor(direction) != null) {
				return game.getDefender(2).getNextDir(game.getAttacker().getLocation().getNeighbor(direction), true);
			}
		}

		return -1;

	}
	public int puppyGuard(Game game)
	{
		Attacker atk = game.getAttacker();
		Defender blinky = game.getDefender(0); //for brevity

		int smallestpath = 1000;
		List<Node> powers = game.getPowerPillList();
		Node closest = null; //used to find the nearest pill

//make sure blinky isnt vulnerable, then make sure there’s a power pill left
		if(blinky.isVulnerable())
			return blinky.getNextDir(atk.getLocation(), false);
		if(powers.isEmpty())
			return blinky.getNextDir(atk.getLocation(), true);
		else
		{ //blinky goes to pacman if both she and it are close to the power pill
			//otherwise blinky goes to the pill
			for (int c = 0; c < powers.size(); c++)
			{
				if (powers.get(c).getPathDistance(atk.getLocation()) <= smallestpath)
				{
					smallestpath = powers.get(c).getPathDistance(atk.getLocation());
					closest = powers.get(c);
				}
			}
			if(closest.getPathDistance((atk.getLocation()))<30&&closest.getPathDistance(blinky.getLocation())<20)
				return blinky.getNextDir(atk.getLocation(), true);
			else return blinky.getNextDir(closest, true);
		}
	}


	public int pinkyGhost(Game game) { //Pink Ghost, or Defender 1
		Attacker pacMan = game.getAttacker(); //This section all for brevity
		Defender herderGhost = game.getDefender(1);
		Node pacmanLocation = pacMan.getLocation();
		Node ghostPen = game.getCurMaze().getInitialDefendersPosition();
		List<Node> powerPillList = game.getCurMaze().getPowerPillNodes();
		Node positionInFrontOfPacman = pacmanLocation.getNeighbor(pacMan.getDirection());
		Node positionBehindPacman = pacmanLocation.getNeighbor(pacMan.getReverse());
		final int  IDEAL_DISTANCE_FROM_PEN = 100; //found to be roughly how far from the pen Pacman is at the start of the game

		if (herderGhost.isVulnerable()) { //if ghost is vulnerable, get away from pacman
			return herderGhost.getNextDir(pacMan.getLocation(), false);
		}
		else if (powerPillList.contains(pacMan.getPossibleLocations(true))) { //if pacman can get to a power pill in the next update, get away from him
			return herderGhost.getNextDir(pacMan.getLocation(),false);
		}
		else if (powerPillList.contains(pacmanLocation)) { //if pacman is hovering on the power pill, get away from him
			return herderGhost.getNextDir(pacMan.getLocation(), false);
		}
		else if (pacmanLocation.getPathDistance(ghostPen) > IDEAL_DISTANCE_FROM_PEN) { //if pacman is further from the pen than 100...
			if (pacMan.getPathTo(ghostPen).contains(pacMan.getPossibleLocations(false))) { //if pacman is facing the ghost pen and can start on a path towards it...
				if (pacMan.getPossibleLocations(true).contains(positionBehindPacman)) {//and if there's a space behind pacman that the ghost can get to...
					return herderGhost.getNextDir(positionBehindPacman, true); //get behind pacman to force him towards the pen
				}
				else {
					return herderGhost.getNextDir(pacMan.getLocation(),true); //just go straight to pacman
				}
			}
			else {
				if (!(pacMan.getPossibleLocations(true).contains(positionBehindPacman))) { //workaround to mean if Pacman can get to tile IN FRONT of him (bugs resulted from saying this directly)
					return herderGhost.getNextDir(positionInFrontOfPacman, true); //get in front of Pacman
				}
				else {
					return herderGhost.getNextDir(pacMan.getLocation(),true); //go directly to pacman
				}
			}
		}
		else {
			return herderGhost.getNextDir(pacmanLocation, true); //go directly to pacman if he's within the ideal distance
		}
	}





	public int[] update(Game game, long timeDue) {

		int[] actions = new int[Game.NUM_DEFENDER];
		//List<Defender> enemies = game.getDefenders();

		actions[3] = kaylasGhost(game);

		actions[2] = nicolesGhost(game);

		actions[0] = puppyGuard(game);

		actions[1] = pinkyGhost(game);

		return actions;


		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts

		/*

		for(int i = 0; i < actions.length; i++)
		{
			Defender defender = enemies.get(i);
			List<Integer> possibleDirs = defender.getPossibleDirs();
			if (possibleDirs.size() != 0)
				actions[i]=possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
			else
				actions[i] = -1;
		}
		return actions;
	}

	*/

		//Make new method for ghost

		//game.getAttacker().getLocation().getX(); //I wrote this

	/*

		private final int Blinky = 0;  (red - Ashlyn - guards power pill)
    	private final int Pinky = 1;   (pink - Michaela - herd pacman towards pen at all times)
    	private final int Inky = 3; (blue - Kayla - follow pacman)
    	private final int Clyde = 2;  (orange - Nicole - get in front of pacman)

		 */

	}

}
