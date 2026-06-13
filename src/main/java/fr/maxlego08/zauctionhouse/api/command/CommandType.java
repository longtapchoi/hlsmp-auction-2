package fr.maxlego08.zauctionhouse.api.command;

public enum CommandType {
   SUCCESS,
   SYNTAX_ERROR,
   EXCEPTION_ERROR,
   DEFAULT,
   CONTINUE;

   // $FF: synthetic method
   private static CommandType[] $values() {
      return new CommandType[]{SUCCESS, SYNTAX_ERROR, EXCEPTION_ERROR, DEFAULT, CONTINUE};
   }
}
