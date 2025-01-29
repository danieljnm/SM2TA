package Uppaal

class GridLayout {
	int horizontalSpacing = 400
	int verticalSpacing = 150
	int labelOffset = 50
	int spacing = 15
	int maxPerRow = 4
	
	def applyLayout(Template template) {
		template.locations.forEach[location, index |
			val row = index / maxPerRow
			val col = index % maxPerRow
			
			location.x = col * horizontalSpacing
			location.y = row * verticalSpacing
			
			if (location.name !== null) {
				location.name.x = location.x - spacing
				location.name.y = location.y + spacing
			}
			
			if (location.label !== null) {
				location.label.x = location.x - spacing
				location.label.y = location.y + spacing * 2
			}
		]
		
		template.applyTransitions
	}
	
	def applyTransitions(Template template) {
		val transitionGroups = template.transitions.groupBy['''«it.source»-->«it.target»''']
		transitionGroups.forEach[key, transitions |
			val source = template.locations.findFirst[it.id == transitions.head.source]
            val target = template.locations.findFirst[it.id == transitions.head.target]
            
            if (source === null || target === null) {
            	return
            }
            
            val midX = (source.x + target.x) / 2
            val midY = (source.y + target.y) / 2
            
            transitions.forEach[transition, index |
            	val offset = index * (transitions.head.labels.size * spacing)
            	transition.labels.forEach [ label, labelIndex |
	                label.x = midX - (source.id === target.id ? 15 : labelOffset)
	                label.y = midY + offset + (labelIndex * spacing) + spacing - (source.id === target.id ? 70 : 0)
            	]
            ]
		]
	}
}