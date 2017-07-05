import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import java.util.ArrayList;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

  get("/", (request,response) -> {
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("places", request.session().attribute("places"));
    model.put("template", "templates/index.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  get("/places", (request, response) -> {
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("places", Place.all());
    model.put("template", "templates/places.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());


  post("/places", (request,response) -> {
    Map<String, Object> model = new HashMap<String, Object>();

    ArrayList<Place> places = request.session().attribute("places");
    if (places == null) {
      places = new ArrayList<Place>();
      request.session().attribute("places", places);
    }

    String placeName = request.queryParams("placeName");
    String placeDescription = request.queryParams("placeDescription");
    Place newPlace = new Place(placeName, placeDescription);
    places.add(newPlace);

    model.put("template", "templates/success.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  get("/places/:id", (request, response) -> {
    Map<String, Object> model = new HashMap<String, Object>();
    Place place = Place.find(Integer.parseInt(request.params(":id")));
    model.put("place", place);
    model.put("template", "templates/place.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  }
}
