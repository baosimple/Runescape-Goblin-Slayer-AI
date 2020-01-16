import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest (
        author = "Bao",
        description = "Defeats goblins in Lumbridge. On respawn, will equip bronze sword and head back to goblin area",
        category = Category.COMBAT,
        version = 1.0,
        name = "Goblin Slayer"
)

public class Main extends AbstractScript {

    public static final String GOBLIN = "Goblin";
    public static final String WEAPON = "Bronze sword";
    public static final Filter<NPC> GOBLIN_FILTER = new Filter<NPC>() {
        @Override
        public boolean match(NPC npc) {
            if(npc == null) {
                return false;
            }

            // health bar visible indicates that the goblin is already in combat with another player and cannot be attacked
            return npc.getName().equals(GOBLIN) && !npc.isHealthBarVisible();
        }
    };
    // coordinates for the goblin slaying spot
    Area killArea = new Area(3243, 3241, 3259, 3231);
    @Override
    public int onLoop() {

        // don't do anything if the player is already in combat
        if(getLocalPlayer().isInCombat()) {
            // do nothing
        }

        // if the player is in the goblin slaying spot
        else if(killArea.contains(getLocalPlayer())) {

            // if equipment slot is empty
            if(getEquipment().isSlotEmpty(EquipmentSlot.WEAPON.getSlot())) {
                // if "Bronze sword" is in inventory
                if(getInventory().contains(WEAPON)) {
                    // Wield weapon
                    getInventory().interact(WEAPON, "Wield");
                }
            }
            else {
                // Find closest NPC that matches the filter
                NPC goblin = getNpcs().closest(GOBLIN_FILTER);
                if (goblin != null) {
                    goblin.interact("Attack");
                }
            }
        }

        // if not in the goblin slaying area, walk there
        else {
            getWalking().walk(killArea.getRandomTile());
        }

        return 1000;
    }
}
