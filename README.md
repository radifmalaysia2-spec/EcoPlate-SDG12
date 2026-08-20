# EcoPlate - Smart Food Waste Reduction Simulator

EcoPlate is a Java Swing application aligned with **UN SDG 12: Responsible Consumption and Production**. It helps a campus cafeteria record food batches, simulate demand, recommend redistribution or composting actions, and measure waste prevented.

**Course:** BIT1123 Object Oriented Programming (Java)  
**Assessment:** Final Group Project (40%)  
**Institution:** City University Malaysia

## Run

```bash
./run.sh
```

On Windows:

```bat
run.bat
```

Java 17 or newer is recommended. Application data is saved automatically to `data/ecoplate-data.txt`.

## OOP and technical coverage

- Encapsulation: private fields with validated constructors and accessors
- Inheritance: `FoodAction` -> `DonationAction` / `CompostAction`
- Abstraction: abstract `FoodAction` and `WasteStrategy` interface
- Runtime polymorphism: recommendations are processed through `FoodAction` references
- Collections: `ArrayList`, `HashMap`, streams and sorting
- File handling: text-file persistence with safe parsing
- GUI: multi-view Swing dashboard with forms, tables, simulation and analytics

## Package structure

- `model` - domain objects and action hierarchy
- `service` - simulation, decision logic and persistence
- `ui` - Swing application
- `util` - validation and formatting helpers

## Project documents

The submission package includes the report, presentation, UML diagram, screenshots and testing evidence.

## Demonstration sequence

1. Add a food batch.
2. Run the customer-demand simulation.
3. Select a batch and review the recommendation.
4. Apply the redistribution or composting action.
5. Open the cumulative impact summary.
