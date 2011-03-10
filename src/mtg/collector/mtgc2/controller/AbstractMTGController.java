package mtg.collector.mtgc2.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import mtg.collector.mtgc2.model.AbstractMTGModel;
import mtg.collector.mtgc2.view.AbstractMTGView;

/**
 *
 * @author Eric Kisner
 */
public abstract class AbstractMTGController implements PropertyChangeListener {

    private ArrayList<AbstractMTGView> registeredViews;
    private ArrayList<AbstractMTGModel> registeredModels;

    public AbstractMTGController() {
        registeredViews = new ArrayList();
        registeredModels = new ArrayList();
    }

    public void addModel( AbstractMTGModel model ) {
        registeredModels.add( model );
        model.addPropertyChangeListener( this );
    }

    public void removeModel( AbstractMTGModel model ) {
        registeredModels.remove( model );
        model.removePropertyChangeListener( this );
    }

    public void addView( AbstractMTGView view ) {
        registeredViews.add( view );
    }

    public void removeView( AbstractMTGView view ) {
        registeredViews.remove( view );
    }

    //  Use this to observe property changes from registered models
    //  and propagate them on to all the views.
    public void propertyChange( PropertyChangeEvent evt ) {
        for( AbstractMTGView view : registeredViews ) {
            view.modelPropertyChange( evt );
        }
    }

    /**
     * This is a convenience method that subclasses can call upon
     * to fire property changes back to the models. This method
     * uses reflection to inspect each of the model classes
     * to determine whether it is the owner of the property
     * in question. If it isn't, a NoSuchMethodException is thrown,
     * which the method ignores.
     *
     * @param propertyName = The name of the property.
     * @param newValue = An object that represents the new value
     * of the property.
     */
    protected void setModelProperty( String propertyName, Object newValue ) {

        for( AbstractMTGModel model : registeredModels ) {
            for( Method method : model.getClass().getMethods() ) {
                if( method.getName().equals( "set" + propertyName ) ) {
                    try {
                        method.invoke( model, newValue );
                        break;
                    } catch( Exception e ) {
                        System.err.println( "Error when invoking: set" + propertyName );
                        e.printStackTrace();
                        System.exit( 1 );
                    }
                }
            }
        }
    }
}
