package generalconstraintsolver.satsubsolver;

import java.util.List;

import checkers.inference.InferenceSolution;
import checkers.inference.model.Slot;
import checkers.inference.model.VariableSlot;
import generalconstraintsolver.GeneralConstrainsSolver;
import generalconstraintsolver.GeneralEncodingSerializer;
import generalconstraintsolver.ImpliesLogic;
import generalconstraintsolver.LatticeGenerator;

public class GeneralSatSolver extends GeneralConstrainsSolver {

    @Override
    protected InferenceSolution solve() {
        LatticeGenerator lattice = new LatticeGenerator(qualHierarchy);
        GeneralEncodingSerializer serializer = new GeneralEncodingSerializer(slotManager, lattice);
        List<ImpliesLogic> allImpliesLogic = serializer.convertAll(constraints);

        SatSubSolver satSolver = new SatSubSolver(allImpliesLogic, slotManager, lattice);
        InferenceSolution solution = satSolver.satSolve();

        if (solution == null) {
            System.out.println("No solution received from the SAT Solver!");
            return null;
        }

        System.out.println("From Sat:");
        System.out.println("/*************** Result from SAT Solver *******************/");
        for (Slot s : this.slots) {
            if (s.getKind() == Slot.Kind.VARIABLE) {
                VariableSlot vs = (VariableSlot) s;
                System.out.println("SlotID: " + vs.getId() + "  " + "Annotation: "
                        + solution.getAnnotation(vs.getId()));
            }
        }
        System.out.flush();
        System.out.println("/**********************************************************/");

        return solution;
    }
}
