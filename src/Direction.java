public enum Direction {
  NORTH {
    @Override
    public String opposite() {
      return "SOUTH";
    }
  },
  SOUTH {
    @Override
    public String opposite() {
      return "NORTH";
    }
  },
  EAST {
    @Override
    public String opposite() {
      return "WEST";
    }
  },
  WEST {
    @Override
    public String opposite() {
      return "EAST";
    }
  };

  public abstract String opposite();
}
