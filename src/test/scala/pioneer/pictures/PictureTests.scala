package pioneer.pictures


import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.nio.file.Files

import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}
import pioneer.resource
import pioneer.pictures.Picture._

class PictureTests extends FunSuite with Matchers with BeforeAndAfter {

  test("Loading, then saving should not change the file") {
    val originalImage = loadImage(resource("/image.png"))

    // save the original file using another name
    val resultPath = PictureTests.makeTemporaryFile()
    saveImage(originalImage, resultPath)

    // load data from saved file
    val resultImage = loadImage(resultPath)

    assertSameImage(originalImage, resultImage)
  }

  test("Loading a non-existent file should raise a FileNotFoundException") {
    val badImagePath = "fake"

    intercept[FileNotFoundException] {
      // try to load image from path
      loadImage(badImagePath)
    }
  }

  test("Loading a non-image file should raise an IllegalArgumentException") {
    val badImagePath = PictureTests.makeTemporaryFile()

    intercept[IllegalArgumentException] {
      // try to load image from path
      loadImage(badImagePath)
    }
  }

  test("Calling flipHorizontal should flip the image horizontally") {
    flipHorizontal(resource("/image.png"), "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("horizontal"), resultImage)
  }

  test("Calling flipHorizontal twice should restore the image") {
    flipHorizontal(resource("/image.png"), "output.png")
    flipHorizontal("output.png", "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("original"), resultImage)
  }

  test("Calling flipVertical should flip the image vertically") {
    flipVertical(resource("/image.png"), "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("vertical"), resultImage)
  }

  test("Calling flipVertical twice should restore the image") {
    flipVertical(resource("/image.png"), "output.png")
    flipVertical("output.png", "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("original"), resultImage)
  }

  test("A sequence of flips: hvhv should restore the image") {
    flipHorizontal(resource("/image.png"), "output.png")
    flipVertical("output.png", "output.png")
    flipHorizontal("output.png", "output.png")
    flipVertical("output.png", "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("original"), resultImage)
  }

  test("Calling rotateLeft should rotate the image left 90 degrees") {
    rotateLeft(resource("/image.png"), "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("left"), resultImage)
  }

  test("Calling rotateLeft four times should restore the image") {
    rotateLeft(resource("/image.png"), "output.png")
    rotateLeft("output.png", "output.png")
    rotateLeft("output.png", "output.png")
    rotateLeft("output.png", "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("original"), resultImage)
  }

  test("Calling rotateLeft three times should rotate the image right 90 degrees") {
    rotateLeft(resource("/image.png"), "output.png")
    rotateLeft("output.png", "output.png")
    rotateLeft("output.png", "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("right"), resultImage)
  }

  test("Calling rotateRight should rotate the image right 90 degrees") {
    rotateRight(resource("/image.png"), "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("right"), resultImage)
  }

  test("Calling rotateRight four times should restore the image") {
    rotateRight(resource("/image.png"), "output.png")
    rotateRight("output.png", "output.png")
    rotateRight("output.png", "output.png")
    rotateRight("output.png", "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("original"), resultImage)
  }

  test("Calling rotateRight three times should rotate the image left 90 degrees") {
    rotateRight(resource("/image.png"), "output.png")
    rotateRight("output.png", "output.png")
    rotateRight("output.png", "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("left"), resultImage)
  }

  test("Calling grayScale turns the image into grayscale") {
    grayScale(resource("/image.png"), "output.png")
    val resultImage = loadImage("output.png")
    assertSameImage(images("grayscale"), resultImage)
  }

  val images: collection.mutable.Map[String, BufferedImage] =
    collection.mutable.Map[String, BufferedImage]()

  before {
    val filename = resource("/image.png")
    val horizontalFilename = resource("/image-horizontal.png")
    val verticalFilename = resource("/image-vertical.png")
    val leftFilename = resource("/image-left.png")
    val rightFilename = resource("/image-right.png")
    val grayscaleFilename = resource("/image-grayscale.png")

    // add the images to the test suite's map of images
    images("original") = loadImage(filename)
    images("horizontal") = loadImage(horizontalFilename)
    images("vertical") = loadImage(verticalFilename)
    images("left") = loadImage(leftFilename)
    images("right") = loadImage(rightFilename)
    images("grayscale") = loadImage(grayscaleFilename)
  }

  def assertSameImage(original: BufferedImage, result: BufferedImage): Unit = {
    assert(original.getWidth === result.getWidth, "(widths differ)")
    assert(original.getHeight === result.getHeight, "(heights differ)")
    val originalData = PictureTests.getImageData(original)
    val resultData = PictureTests.getImageData(result)
    originalData should contain theSameElementsInOrderAs resultData
  }

}

object PictureTests {
  def makeTemporaryFile(): String =
    Files.createTempFile("result", "png").toAbsolutePath.toString

  def getImageData(image: BufferedImage): Seq[Int] = {
    val buffer = image.getRaster.getDataBuffer
    (0 until buffer.getSize).map(buffer.getElem(_))
  }
}
