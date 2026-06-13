package fr.maxlego08.zauctionhouse.api.messages;

import fr.maxlego08.zauctionhouse.api.AuctionPlugin;
import fr.maxlego08.zauctionhouse.api.messages.messages.ClassicMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum Message {
   PREFIX("<primary>zAuctionHouse <secondary>• "),
   VERSION_AVAILABLE("<#ff0000>There is a new version of zAuctionHouse available ! <gray>(<white>current: <#ff9900>%version% <gray>| <white>latest: <#00ff00>%latest%<gray>)"),
   COMMAND_SYNTAX_ERROR("<error>You must execute the command like this<gray>: <success>%syntax%"),
   COMMAND_NO_PERMISSION("<error>You do not have permission to run this command."),
   COMMAND_NO_CONSOLE("<error>Only one player can execute this command."),
   COMMAND_NO_ARG("<error>Impossible to find the command with its arguments."),
   COMMAND_RESTRICTED("<error>You cannot use this command here."),
   COMMAND_COOLDOWN("<error>Please wait before using this command again."),
   COMMAND_SYNTAX_HELP("<white>%syntax% <dark_gray>» <gray>%description%"),
   INVENTORY_NOT_FOUND("<error>Impossible to find the inventory <white>%inventory-name%<error>."),
   COMMAND_DESCRIPTION_AUCTION("Open auction house"),
   COMMAND_DESCRIPTION_AUCTION_SELL("Add an item to the sale"),
   COMMAND_DESCRIPTION_AUCTION_RENT("Add an item for rent"),
   COMMAND_DESCRIPTION_AUCTION_BID("Add an item to the auction"),
   COMMAND_DESCRIPTION_AUCTION_RELOAD("Reload configurations files"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN("Open administrative tools for auctions"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_GENERATE("Generate fake items"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_OPEN("Open current auction items"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_FORCEOPEN("Open any inventory for a player"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_HISTORY("Open player auction history"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_ADD("Add an item to the auction"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE("Manage player cache"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE_SET("Set a value in player cache"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE_CLEAR("Clear player cache"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE_SHOW("Show player cache"),
   COMMAND_DESCRIPTION_AUCTION_CLAIM("Claim pending money from sales"),
   COMMAND_DESCRIPTION_AUCTION_PAGE("Open auction house at a specific page"),
   SELL_ERROR_AIR("<error>Are you stupid ? You can't sell air !"),
   SELL_ERROR_CHANGE("<error>The item in your hand has changed, sale cancelled."),
   SELL_ERROR_ECONOMY("<error>Unable to find the economy <white>%name%<error>."),
   SELL_ERROR_DEFAULT_ECONOMY("<error>No default economy is configured. Please contact an administrator."),
   SELL_INVENTORY_EMPTY("<error>You must place items in the inventory before confirming."),
   SELL_INVENTORY_CANCELLED("<error>You cancelled the sale, your items have been returned."),
   SELL_ITEMS_CLEARED("<success>Selected items have been cleared."),
   SELL_ITEM_ADDED("<success>Item added to sale list."),
   SELL_ITEM_REMOVED("<success>Item removed from sale list."),
   ADMIN_TARGET_REQUIRED("<error>You must specify a valid target player."),
   ADMIN_TARGET_NOT_FOUND("<error>Unable to find the player <white>%target%<error>."),
   ADMIN_OPEN_INVENTORY("<success>Opening %type% items for <white>%target%<success>."),
   ADMIN_FORCEOPEN_INVENTORY("<success>Opening inventory <white>%inventory%<success> for <white>%target%<success> at page <white>%page%<success>."),
   ADMIN_OPEN_HISTORY("<success>Opening history for <white>%target%<success>."),
   ADMIN_ITEM_REMOVED("<success>You removed <white>%items%<success> from <white>%target%<success>."),
   ADMIN_ITEM_ADDED("<success>You added <white>%items%<success> to <white>%target%<success> in <white>%type%<success>."),
   ADMIN_ITEM_RETRIEVED("<success>Item(s) retrieved successfully."),
   ADMIN_NO_ITEM_TO_RETRIEVE("<error>No item to retrieve from this log entry."),
   RELOAD_SUCCESS("<success>You just reloaded the plugin !"),
   ITEM_REMOVE_LISTED("#e6fff3You just removed %items% #e6fff3from the listed items."),
   ITEM_REMOVE_EXPIRED("#e6fff3You just removed %items% #e6fff3from the expired items."),
   ITEM_REMOVE_PURCHASED("#e6fff3You just removed %items% #e6fff3from the purchased items."),
   ITEM_REMOVE_SELLING("#e6fff3You just removed %items% #e6fff3from your items."),
   ITEM_SOLD("#e6fff3You just sold %items% #e6fff3for #92bed8%price%#e6fff3."),
   ITEM_BOUGHT_SELLER("#ffacd5%buyer% #e6fff3just bought %items% #e6fff3for #92bed8%price%#e6fff3."),
   ITEM_BOUGHT_BUYER("#e6fff3You have just bought %items% #e6fff3for #92bed8%price%#e6fff3."),
   NOT_ENOUGH_MONEY("<error>You don’t have enough money to buy this."),
   NOT_ENOUGH_SPACE("<error>You don't have enough space in your inventory."),
   PRICE_TOO_HIGH("<error>You cannot sell for more than <white>%max-price%<error>."),
   PRICE_TOO_LOW("<error>You cannot sell for less than <white>%min-price%<error>."),
   LISTED_ITEMS_LIMIT("<error>You cannot sell more than <white>%max-items%<error> items<error>. &8(&7Did you set the zauctionhouse.<number in config.yml> ?&8)"),
   WORLD_BANNED("<error>You cannot sell items in this world."),
   ITEM_BLACKLISTED("<error>You cannot sell blacklisted items."),
   ITEM_WHITELISTED("<error>You cannot sell an item that is not whitelist."),
   ADMIN_GENERATE_WARNING(new String[]{"<error>⚠ WARNING ⚠", "<error>This command will generate <white>%amount%<error> fake auction items for performance testing.", "<error>This data is <white>FAKE<error> and will need to be reset after testing.", "<error>Execute this command again within <white>30 seconds<error> to confirm."}),
   ADMIN_GENERATE_CONFIRMED("<success>Generation confirmed! Creating <white>%amount%<success> auction items..."),
   ADMIN_GENERATE_PLAYERS_START("<gray>Registering <white>%total%<gray> unique players..."),
   ADMIN_GENERATE_PLAYERS_PROGRESS("<gray>Players: <white>%current%<gray>/<white>%total%<gray> registered..."),
   ADMIN_GENERATE_PLAYERS_COMPLETE("<success>Registered <white>%amount%<success> unique players."),
   ADMIN_GENERATE_ITEMS_START("<gray>Creating <white>%total%<gray> auction items..."),
   ADMIN_GENERATE_PROGRESS("<gray>Progress: <white>%current%<gray>/<white>%total%<gray> items created..."),
   ADMIN_GENERATE_COMPLETE("<success>Successfully generated <white>%amount%<success> auction items in <white>%time%ms<success>."),
   ADMIN_GENERATE_NO_MATERIALS("<error>No valid materials found in categories. Make sure you have categories other than 'misc' configured."),
   ADMIN_GENERATE_INVALID_AMOUNT("<error>Invalid amount. Please specify a number between <white>1<error> and <white>100000<error>."),
   ADMIN_CACHE_SHOW_HEADER("<primary>Cache for <white>%player%<primary>:"),
   ADMIN_CACHE_SHOW_ENTRY("<gray>  %key% <dark_gray>» <white>%value%"),
   ADMIN_CACHE_SHOW_EMPTY("<gray>  No cache entries found."),
   ADMIN_CACHE_CLEARED("<success>Cleared cache key <white>%key%<success> for <white>%player%<success>."),
   ADMIN_CACHE_CLEARED_ALL("<success>Cleared all cache for <white>%player%<success>."),
   ADMIN_CACHE_CLEARED_ALL_PLAYERS("<success>Cleared cache key <white>%key%<success> for <white>%count%<success> players."),
   ADMIN_CACHE_CLEARED_ALL_PLAYERS_ALL("<success>Cleared all cache for <white>%count%<success> players."),
   ADMIN_CACHE_SET("<success>Set <white>%key%<success> to <white>%value%<success> for <white>%player%<success>."),
   ADMIN_CACHE_SET_ALL_PLAYERS("<success>Set <white>%key%<success> to <white>%value%<success> for <white>%count%<success> players."),
   ADMIN_CACHE_INVALID_KEY("<error>Invalid cache key <white>%key%<error>. Use tab completion for valid keys."),
   ADMIN_CACHE_INVALID_VALUE("<error>Invalid value <white>%value%<error> for key <white>%key%<error>."),
   ADMIN_CACHE_KEY_NOT_SETTABLE("<error>Cache key <white>%key%<error> cannot be set from command."),
   ADMIN_CACHE_PLAYER_NOT_ONLINE("<error>Player <white>%player%<error> is not online."),
   CLAIM_NO_PENDING("<error>You have no pending money to claim."),
   CLAIM_SUCCESS("<success>You have successfully claimed your pending money!"),
   CLAIM_ECONOMY_SUCCESS("<success>You received <white>%amount%<success> from <white>%economy%<success>."),
   CLAIM_PENDING_NOTIFY("#e6fff3You have pending money to claim! Use <white>/ah claim<#e6fff3> to receive <white>%amount%<#e6fff3>."),
   SALES_NOTIFICATION(new String[]{"<click:run_command:/ah history>#e6fff3While you were away, <white>%count%<#e6fff3> of your items were sold for a total of <white>%total%<#e6fff3>!", "<click:run_command:/ah history>#8c8c8c• #2CCED2Click here<#92ffff> to view your sales history</click>"}),
   TAX_SELL_APPLIED("<gray>A tax of <white>%tax%<gray> (%percentage%%) has been applied to this sale."),
   TAX_PURCHASE_APPLIED("<gray>A tax of <white>%tax%<gray> (%percentage%%) has been applied to this purchase."),
   TAX_CAPITALISM_INFO("<gray>The price includes <white>%tax%<gray> VAT (%percentage%%)."),
   TAX_EXEMPT("<green>You are exempt from taxes."),
   TAX_REDUCED("<green>You benefit from a reduced tax rate (%percentage%%)."),
   TAX_INSUFFICIENT_FUNDS("<error>You don't have enough money to pay the tax of <white>%tax%<error>."),
   COMMAND_DESCRIPTION_AUCTION_MIGRATE("Migrate data from another auction plugin"),
   MIGRATION_NOT_CONFIGURED("<error>Migration for <white>%source%<error> is not configured. Please configure the <white>migration<error> section in config.yml."),
   MIGRATION_INVALID_SOURCE("<error>Unknown migration source: <white>%source%"),
   MIGRATION_AVAILABLE_SOURCES("<gray>Available sources: %sources%"),
   MIGRATION_INFO(new String[]{"<primary>Migration from <white>%source%", "<gray>Details: <white>%details%"}),
   MIGRATION_CONFIRM(new String[]{"<gray>Run <white>/ah admin migrate %source% confirm<gray> to start the migration.", "<error>⚠ WARNING: This will import data into V4. Make sure to backup your data first!"}),
   MIGRATION_STARTED("<success>Migration from <white>%source%<success> started... This may take a while."),
   MIGRATION_PROGRESS("<gray>[Migration] %progress%"),
   MIGRATION_SUCCESS(new String[]{"<success>Migration from <white>%source%<success> completed successfully!", "<gray>  Players: <white>%players%", "<gray>  Items: <white>%items%", "<gray>  Transactions: <white>%transactions%", "<gray>  Errors: <white>%errors%", "<gray>  Duration: <white>%duration%ms"}),
   MIGRATION_FAILED("<error>Migration failed: <white>%error%"),
   SEARCH_START("<#8a8a8a>Please type your search in the chat."),
   SEARCH_CLEARED("<#8a8a8a>Search cleared."),
   SEARCH_NO_RESULTS("<#8a8a8a>No items found for <#ffffff>%query%<#8a8a8a>."),
   SEARCH_SEARCHING("<#8a8a8a>Searching for <#ffffff>%query%<#8a8a8a>..."),
   COMMAND_DESCRIPTION_AUCTION_SEARCH("Search for items in the auction house"),
   BROADCAST_SELL("#e6fff3<white>%seller% <#e6fff3>just listed %items% <#e6fff3>for <#92bed8>%price%<#e6fff3>."),
   BROADCAST_PURCHASE("#e6fff3<white>%buyer% <#e6fff3>just purchased %items% <#e6fff3>for <#92bed8>%price%<#e6fff3>."),
   COMMAND_DESCRIPTION_AUCTION_OPTION("Manage your auction house options"),
   OPTION_BROADCAST_SELL_ENABLED("<success>Sell broadcasts have been <white>enabled<success>."),
   OPTION_BROADCAST_SELL_DISABLED("<success>Sell broadcasts have been <white>disabled<success>."),
   OPTION_BROADCAST_PURCHASE_ENABLED("<success>Purchase broadcasts have been <white>enabled<success>."),
   OPTION_BROADCAST_PURCHASE_DISABLED("<success>Purchase broadcasts have been <white>disabled<success>."),
   OPTION_LIST_HEADER("<primary>Your options:"),
   OPTION_LIST_ENTRY("<gray>  %option% <dark_gray>> <white>%value%"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION("Manage player options"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION_SET("Set a player option"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION_LIST("List player options"),
   COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION_RESET("Reset a player's options to defaults"),
   ADMIN_OPTION_SET("<success>Set option <white>%option%<success> to <white>%value%<success> for <white>%player%<success>."),
   ADMIN_OPTION_RESET("<success>All options for <white>%player%<success> have been reset to defaults."),
   ADMIN_OPTION_LIST_HEADER("<primary>Options for <white>%player%<primary>:"),
   REMOVE_ALL_ITEMS("#e6fff3You have retrieved <white>%amount%<#e6fff3> item(s).");

   private AuctionPlugin plugin;
   private List<AuctionMessage> messages;

   private Message(String var3) {
      this(MessageType.TCHAT, var3);
   }

   private Message(MessageType var3, String var4) {
      this.messages = new ArrayList();
      this.messages.add(new ClassicMessage(var3, Collections.singletonList(var4)));
   }

   private Message(String... var3) {
      this(MessageType.TCHAT, var3);
   }

   private Message(MessageType var3, String... var4) {
      this.messages = new ArrayList();
      this.messages.add(new ClassicMessage(var3, Arrays.asList(var4)));
   }

   private Message(AuctionMessage... var3) {
      this.messages = new ArrayList();
      this.messages = Arrays.asList(var3);
   }

   public static Message fromString(String var0) {
      try {
         return valueOf(var0);
      } catch (Exception var2) {
         return null;
      }
   }

   public List<AuctionMessage> getMessages() {
      return this.messages;
   }

   public void setMessages(List<AuctionMessage> var1) {
      this.messages = var1;
   }

   public String toConfigurationName() {
      return this.name().replace("_", "-").toLowerCase();
   }

   public String getMessageAsString() {
      String var1 = this.toConfigurationName();
      if (this.messages.isEmpty()) {
         this.plugin.getLogger().severe(var1 + " is empty ! Check your configuration");
         return "Error with " + var1 + ", check your console";
      } else {
         AuctionMessage var2 = (AuctionMessage)this.messages.getFirst();
         if (var2 instanceof ClassicMessage) {
            ClassicMessage var3 = (ClassicMessage)var2;
            if (var3.messages().isEmpty()) {
               this.plugin.getLogger().severe(var1 + " message is empty ! Check your configuration");
               return "Error with " + var1 + ", check your console";
            } else {
               return (String)var3.messages().getFirst();
            }
         } else {
            this.plugin.getLogger().severe(var1 + " is not a tchat message ! Check your configuration");
            return "Error with " + var1 + ", check your console";
         }
      }
   }

   public void setPlugin(AuctionPlugin var1) {
      this.plugin = var1;
   }

   public List<String> getMessageAsStringList() {
      return this.messages.stream().filter((var0) -> var0 instanceof ClassicMessage).map((var0) -> (ClassicMessage)var0).map(ClassicMessage::messages).flatMap(Collection::stream).toList();
   }

   // $FF: synthetic method
   private static Message[] $values() {
      return new Message[]{PREFIX, VERSION_AVAILABLE, COMMAND_SYNTAX_ERROR, COMMAND_NO_PERMISSION, COMMAND_NO_CONSOLE, COMMAND_NO_ARG, COMMAND_RESTRICTED, COMMAND_COOLDOWN, COMMAND_SYNTAX_HELP, INVENTORY_NOT_FOUND, COMMAND_DESCRIPTION_AUCTION, COMMAND_DESCRIPTION_AUCTION_SELL, COMMAND_DESCRIPTION_AUCTION_RENT, COMMAND_DESCRIPTION_AUCTION_BID, COMMAND_DESCRIPTION_AUCTION_RELOAD, COMMAND_DESCRIPTION_AUCTION_ADMIN, COMMAND_DESCRIPTION_AUCTION_ADMIN_GENERATE, COMMAND_DESCRIPTION_AUCTION_ADMIN_OPEN, COMMAND_DESCRIPTION_AUCTION_ADMIN_FORCEOPEN, COMMAND_DESCRIPTION_AUCTION_ADMIN_HISTORY, COMMAND_DESCRIPTION_AUCTION_ADMIN_ADD, COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE, COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE_SET, COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE_CLEAR, COMMAND_DESCRIPTION_AUCTION_ADMIN_CACHE_SHOW, COMMAND_DESCRIPTION_AUCTION_CLAIM, COMMAND_DESCRIPTION_AUCTION_PAGE, SELL_ERROR_AIR, SELL_ERROR_CHANGE, SELL_ERROR_ECONOMY, SELL_ERROR_DEFAULT_ECONOMY, SELL_INVENTORY_EMPTY, SELL_INVENTORY_CANCELLED, SELL_ITEMS_CLEARED, SELL_ITEM_ADDED, SELL_ITEM_REMOVED, ADMIN_TARGET_REQUIRED, ADMIN_TARGET_NOT_FOUND, ADMIN_OPEN_INVENTORY, ADMIN_FORCEOPEN_INVENTORY, ADMIN_OPEN_HISTORY, ADMIN_ITEM_REMOVED, ADMIN_ITEM_ADDED, ADMIN_ITEM_RETRIEVED, ADMIN_NO_ITEM_TO_RETRIEVE, RELOAD_SUCCESS, ITEM_REMOVE_LISTED, ITEM_REMOVE_EXPIRED, ITEM_REMOVE_PURCHASED, ITEM_REMOVE_SELLING, ITEM_SOLD, ITEM_BOUGHT_SELLER, ITEM_BOUGHT_BUYER, NOT_ENOUGH_MONEY, NOT_ENOUGH_SPACE, PRICE_TOO_HIGH, PRICE_TOO_LOW, LISTED_ITEMS_LIMIT, WORLD_BANNED, ITEM_BLACKLISTED, ITEM_WHITELISTED, ADMIN_GENERATE_WARNING, ADMIN_GENERATE_CONFIRMED, ADMIN_GENERATE_PLAYERS_START, ADMIN_GENERATE_PLAYERS_PROGRESS, ADMIN_GENERATE_PLAYERS_COMPLETE, ADMIN_GENERATE_ITEMS_START, ADMIN_GENERATE_PROGRESS, ADMIN_GENERATE_COMPLETE, ADMIN_GENERATE_NO_MATERIALS, ADMIN_GENERATE_INVALID_AMOUNT, ADMIN_CACHE_SHOW_HEADER, ADMIN_CACHE_SHOW_ENTRY, ADMIN_CACHE_SHOW_EMPTY, ADMIN_CACHE_CLEARED, ADMIN_CACHE_CLEARED_ALL, ADMIN_CACHE_CLEARED_ALL_PLAYERS, ADMIN_CACHE_CLEARED_ALL_PLAYERS_ALL, ADMIN_CACHE_SET, ADMIN_CACHE_SET_ALL_PLAYERS, ADMIN_CACHE_INVALID_KEY, ADMIN_CACHE_INVALID_VALUE, ADMIN_CACHE_KEY_NOT_SETTABLE, ADMIN_CACHE_PLAYER_NOT_ONLINE, CLAIM_NO_PENDING, CLAIM_SUCCESS, CLAIM_ECONOMY_SUCCESS, CLAIM_PENDING_NOTIFY, SALES_NOTIFICATION, TAX_SELL_APPLIED, TAX_PURCHASE_APPLIED, TAX_CAPITALISM_INFO, TAX_EXEMPT, TAX_REDUCED, TAX_INSUFFICIENT_FUNDS, COMMAND_DESCRIPTION_AUCTION_MIGRATE, MIGRATION_NOT_CONFIGURED, MIGRATION_INVALID_SOURCE, MIGRATION_AVAILABLE_SOURCES, MIGRATION_INFO, MIGRATION_CONFIRM, MIGRATION_STARTED, MIGRATION_PROGRESS, MIGRATION_SUCCESS, MIGRATION_FAILED, SEARCH_START, SEARCH_CLEARED, SEARCH_NO_RESULTS, SEARCH_SEARCHING, COMMAND_DESCRIPTION_AUCTION_SEARCH, BROADCAST_SELL, BROADCAST_PURCHASE, COMMAND_DESCRIPTION_AUCTION_OPTION, OPTION_BROADCAST_SELL_ENABLED, OPTION_BROADCAST_SELL_DISABLED, OPTION_BROADCAST_PURCHASE_ENABLED, OPTION_BROADCAST_PURCHASE_DISABLED, OPTION_LIST_HEADER, OPTION_LIST_ENTRY, COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION, COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION_SET, COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION_LIST, COMMAND_DESCRIPTION_AUCTION_ADMIN_OPTION_RESET, ADMIN_OPTION_SET, ADMIN_OPTION_RESET, ADMIN_OPTION_LIST_HEADER, REMOVE_ALL_ITEMS};
   }
}
