ALTER TABLE public.portti_wfs_layers_styles
    DROP CONSTRAINT portti_wfs_layers_styles_wfs_layer_fkey,
    ADD CONSTRAINT portti_wfs_layers_styles_wfs_layer_fkey FOREIGN KEY (wfs_layer_id)
    REFERENCES public.portti_wfs_layer (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE;