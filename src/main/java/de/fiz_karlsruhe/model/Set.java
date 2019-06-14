package de.fiz_karlsruhe.model;

public class Set {

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((spec == null) ? 0 : spec.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Set other = (Set) obj;
    if (spec == null) {
      if (other.spec != null)
        return false;
    } else if (!spec.equals(other.spec))
      return false;
    return true;
  }

  private String name;
  
  private String spec;
  
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSpec() {
    return spec;
  }

  public void setSpec(String spec) {
    this.spec = spec;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Set [name=" + name + ", spec=" + spec + ", description=" + description + "]";
  }
  
  
}
