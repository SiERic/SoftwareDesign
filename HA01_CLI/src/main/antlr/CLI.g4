grammar CLI;

@parser::members
{
  public java.util.HashMap<String, Double> memory = new java.util.HashMap<String, Double>();
  @Override
  public void notifyErrorListeners(Token offendingToken, String msg, RecognitionException ex)
  {
    throw new RuntimeException(msg);
  }
}

@lexer::members
{
  @Override
  public void recover(RecognitionException ex)
  {
    throw new RuntimeException(ex.getMessage());
  }
}

operation
    : SPACE* (commands += command SPACE* ('|' SPACE* commands += command SPACE*)*)? EOF #setOfCommands
    | variable=NO_QUOTE_TOKEN SPACE* '=' SPACE* value=token EOF #assignment
    ;

command
    : commandName=token (SPACE* args += token)*
    ;

token
    : INSIDE_DQUOTES #doubleQuote
    | INSIDE_SQUOTES #singleQuote
    | NO_QUOTE_TOKEN #unquote
    ;


INSIDE_DQUOTES
    : '"' (~'"')* '"'
    ;

INSIDE_SQUOTES
    : '\'' (~'\'')* '\''
    ;

NO_QUOTE_TOKEN
    : ~(' ' | '\t' | '\n' | '\r' | '\'' | '"' | '|' | '=')+
    ;

SPACE
    : ' '
    | '\t'
    ;
