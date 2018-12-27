package rpg.core;

import java.util.ArrayList;
import rpg.core.interfaces.IItem;
import rpg.core.objects.Slots;
import rpg.helpers.State;

/**
 * Storage for all crafting recipies that are available. The subclass defines
 * the structure of such a
 * @author malub
 */
final class Recipes {

	public class CraftingRecipe {
		private ArrayList<IItem>	inputItems;
		private ArrayList<IItem>	outputItems;
		private int					requiredLevel;

		public CraftingRecipe(ArrayList<IItem> from, ArrayList<IItem> to, int requiredLevel) {
			this.inputItems = from;
			this.outputItems = to;
			this.requiredLevel = requiredLevel;
		}

		/**
		 * Will craft by this recipe as many times as possible but at most
		 * {@code amount} times.
		 * @param storage The Slots to be used as storage for crafting. Input items will
		 * be taken from it and output items will be put into it.
		 * @param amount the amount of crafting to do at max.
		 * @return finished if all craftings are executed, earlyExit if at least one
		 * crafting could not be executed, failed if something goes wrong.
		 */
		public State craft(Slots<IItem> storage, int amount) {
			State lastState = State.finished;
			for (int i = 0; i < amount; ++i) {
				lastState = craft(storage);
				if (lastState != State.finished) {
					if (lastState == State.failed) return State.failed;
					return State.earlyExit;
				}
			}
			return State.finished;
		}

		public State craft(Slots<?> storage) {
			if (storage.hasAll(inputItems)) {
				storage.removeAll(inputItems);
				boolean enoughSpace = storage.addAll(outputItems);

				if (!enoughSpace) {
					// if the items couldn't be added
					storage.addAll(inputItems);
					return State.notAllowed;
				}
				return State.finished;
			}

			return State.notAllowed;
		}
	}

}
