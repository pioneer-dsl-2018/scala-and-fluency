package object pioneer {
  /**
    * Use this function to get a path to images provided with this project. For
    * example, if you want to access the `image.png` file provided in the
    * `/src/main/resources` directory, you would call:
    * {{{resources("/image.png")}}}
    * @param filename the path (relative to `resources`) to the image file
    * @return absolute path to the provided image file
    */
  def resource(filename: String): String = {
    getClass.getResource(filename).getPath
  }
}