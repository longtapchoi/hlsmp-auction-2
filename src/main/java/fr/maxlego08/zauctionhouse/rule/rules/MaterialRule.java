package fr.maxlego08.zauctionhouse.rule.rules;

import fr.maxlego08.zauctionhouse.api.rules.ItemRuleContext;
import fr.maxlego08.zauctionhouse.api.rules.Rule;
import java.util.Set;
import org.bukkit.Material;

public class MaterialRule implements Rule {
   private final Set<Material> materials;

   public MaterialRule(Set<Material> var1) {
      this.materials = var1;
   }

   public boolean matches(ItemRuleContext var1) {
      return this.materials.contains(var1.getMaterial());
   }

   public boolean isValid() {
      return !this.materials.isEmpty();
   }
}
